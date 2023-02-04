import time
import datetime
from flask import Flask, request, make_response
import websocket
import stomper
import json
import threading
import oscpDriver

TOPICS = ["BootNotification", "StatusNotification", "MeterValues", "StartTransaction", "StopTransaction",
          "ChangeAvailability", "ClearChargingProfile", "CancelReservation", "RemoteStartTransaction",
          "RemoteStopTransaction", "ReserveNow", "SetChargingProfile"]

MESSAGES_TO_SEND = ["StatusNotification", "MeterValues", "StartTransaction", "StopTransaction"]

CONTENT_TYPE = "application/json"

MAPPING = {"ChangeAvailability/topic/ocpp/ChangeAvailability": "ChangeAvailability",
           "CancelReservation/topic/ocpp/CancelReservation": "CancelReservation",
           "ReserveNow/topic/ocpp/ReserveNow": "ReserveNow",
           "SetChargingProfile/topic/ocpp/SetChargingProfile": "SetChargingProfile",
           "ClearChargingProfile/topic/ocpp/ClearChargingProfile": "ClearChargingProfile"}


class CpConnection:

    def __init__(self, cp_id, auth_key):
        self.session_id = None
        self.cp_id = cp_id
        self.websock = None
        self.auth_key = auth_key

    def create_connection(self):
        # self.websock = websocket.WebSocketApp(f"ws://localhost:8080/ocpp?token={self.auth_key}",
        self.websock = websocket.WebSocketApp(f"wss://cpmsserver.up.railway.app/ocpp?token={self.auth_key}",
                                              on_open=self.on_open,
                                              on_message=self.on_message,
                                              on_close=self.on_close,
                                              on_error=self.on_error)
        self.websock.run_forever()

    def on_open(self, ws: websocket.WebSocketApp):
        self.websock.send("CONNECT\naccept-version:1.0,1.1,2.0\n\n\x00\n")
        sub_msg = stomper.subscribe("/topic/ocpp/BootNotification", self.cp_id)
        self.websock.send(sub_msg)
        self.websock.send(stomper.send("/app/ocpp/BootNotification",
                                       json.dumps({"cpId": self.cp_id}),
                                       content_type=CONTENT_TYPE))

    def on_message(self, ws, message):
        message_unpacked = stomper.unpack_frame(message)
        body = json.loads(message_unpacked["body"])
        destination: str = message_unpacked["headers"]["destination"]
        print(f"""
==========================================================================================
current time: {datetime.datetime.now().isoformat()}
message from cp: {self.cp_id} at destination {destination}
the message is: 
{message}
The message parsed is 
{message_unpacked}
                """)
        if destination == "/topic/ocpp/BootNotification":
            self.on_boot_notification_conf(body)
            return
        prefix = len("/topic/" + self.cp_id)
        destination = destination[prefix:]
        print("corrected destination: " + destination)
        if destination in ["ChangeAvailability/topic/ocpp/ChangeAvailability",
                           "CancelReservation/topic/ocpp/CancelReservation",
                           "ReserveNow/topic/ocpp/ReserveNow",
                           "SetChargingProfile/topic/ocpp/SetChargingProfile",
                           "ClearChargingProfile/topic/ocpp/ClearChargingProfile"]:
            request_id = message_unpacked["headers"]["requestId"]
            self.on_message_to_accept("/app/ocpp/" + MAPPING[destination] + "Conf", request_id)
        elif destination == "RemoteStartTransaction/topic/ocpp/RemoteStartTransaction":
            request_id = message_unpacked["headers"]["requestId"]
            t = threading.Thread(target=self.on_remote_start_transaction, args=(body, request_id))
            t.start()
            #self.on_remote_start_transaction(body, request_id)
        elif destination == "RemoteStopTransaction/topic/ocpp/RemoteStopTransaction":
            request_id = message_unpacked["headers"]["requestId"]
            self.on_remote_stop_transaction(body, request_id)

    def on_close(self, ws, a, b):
        print(f'''
==========================================================================================
Connection with {self.cp_id} (session id = {self.session_id}) closed
Trying to reconnect
==========================================================================================
        ''')
        time.sleep(600)
        self.create_connection()

    def on_error(self, ws, message):
        print(f"error {message}: current time = {datetime.datetime.now().isoformat()}")

    def close_connection(self):
        self.websock.close()

    def on_boot_notification_conf(self, message):
        self.session_id = message["sessionId"]
        for topic in TOPICS:
            sub_msg = stomper.subscribe(f"/topic/{self.cp_id + topic}/topic/ocpp/{topic}", self.cp_id + topic)
            self.websock.send(sub_msg)

    def on_message_to_accept(self, topic, request_id):
        print("sending ACCEPTED as a response")
        res_message = {"commandResult": "ACCEPTED", "requestId": request_id}
        stomp_message = stomper.send(topic, json.dumps(res_message), content_type=CONTENT_TYPE)
        self.websock.send(stomp_message)

    def on_remote_start_transaction(self, message, request_id):
        self.on_message_to_accept("/app/ocpp/RemoteStartTransactionConf", request_id)
        time.sleep(5)
        connector_id = message['connectorId']
        reservation_id = message['reservationId']
        start_tr_msg = {
            "connectorId": connector_id,
            "reservationId": reservation_id,
            "timestamp": datetime.datetime.now().isoformat()
        }
        print(f"sending StartTransaction to cp: {self.cp_id}")
        stomp_msg = stomper.send("/app/ocpp/StartTransaction", json.dumps(start_tr_msg), content_type=CONTENT_TYPE)
        self.websock.send(stomp_msg)
        meter_value_msg = {
            "connectorId": connector_id,
            "meterValue": [{"timestamp": datetime.datetime.now().isoformat(), "sampledValue": 5.0}]
        }
        for _ in range(3):
            time.sleep(100)
            stomp_msg = stomper.send("/app/ocpp/MeterValue", json.dumps(meter_value_msg), content_type=CONTENT_TYPE)
            print(f"Sending meter value to cp: {self.cp_id}")
            self.websock.send(stomp_msg)
        time.sleep(100)
        stop_tr_msg = {
            "transactionId": reservation_id,
            "timestamp": datetime.datetime.now().isoformat()
        }
        stomp_msg = stomper.send("/app/ocpp/StopTransaction", json.dumps(stop_tr_msg), content_type=CONTENT_TYPE)
        self.websock.send(stomp_msg)

    def on_remote_stop_transaction(self, message, request_id):
        self.on_message_to_accept("/app/ocpp/RemoteStopTransactionConf", request_id)
        time.sleep(10)
        stop_tr_msg = {
            "transactionId": message['transactionId'],
            "timestamp": datetime.datetime.now().isoformat()
        }
        print("Sending stop transaction")
        stomp_msg = stomper.send("/app/ocpp/StopTransaction", json.dumps(stop_tr_msg), content_type=CONTENT_TYPE)
        self.websock.send(stomp_msg)

    def send_msg(self, topic, message):
        stomp_message = stomper.send(topic, message, content_type="application/json")
        self.websock.send(stomp_message)


# can only be run in local with access to the command line
class InteractiveConnection:

    def __init__(self):
        self.connections = dict()
        self.cps = dict()
        self.threads = dict()

    def add_connection(self, cp_id, auth_key, cp):
        cp_connection = CpConnection(cp_id, auth_key)
        self.connections[cp_id] = cp_connection
        self.cps[cp_id] = cp
        thread = threading.Thread(target=cp_connection.create_connection)
        thread.start()

    def close_connection(self, cp_id):
        self.connections[cp_id].close_connection()

    def close_all(self):
        for cp_id in self.connections.keys():
            self.close_connection(cp_id)

    def loop(self):
        while True:
            time.sleep(60)
            if len(self.connections.keys()) > 0:
                print(f"select a cp to send message: {self.connections.keys()}, or quit to end the driver")
                input_ = input("> ")
                if input_ == "quit":
                    self.close_all()
                    return
                elif input_ not in self.connections.keys():
                    print("wrong input")
                else:
                    self.build_message(input_)

    def build_message(self, cp_id):
        print(f"this is the cp that you have chosen: {self.cps[cp_id]}")
        print(f"select a message to send upon: {MESSAGES_TO_SEND}")
        message_type = input("> ")
        if message_type not in MESSAGES_TO_SEND:
            print("wrong input")
        elif message_type == "StatusNotification":
            self.build_status_notification(cp_id)

    def build_status_notification(self, cp_id):
        message = dict()
        message["connector_id"] = input("insert connector id: > ")
        message["error_code"] = input("insert error code: > ")
        message["status"] = input("insert status: > ")
        message["timestamp"] = datetime.datetime.now().isoformat()
        self.connections[cp_id].send_msg("/app/ocpp/StatusNotification", json.dumps(message))


app = Flask(__name__)
interactive_connections = InteractiveConnection()


@app.route("/open_connection", methods=["POST"])
def open_connection():
    auth_key = request.args.get("token")
    cp_id = request.args.get("cp_id")
    cp = request.json
    print(f"trying to connect to {cp_id}")
    interactive_connections.add_connection(cp_id, auth_key, cp)
    threading.Thread(target=oscpDriver.OscpConnection, args=(cp_id,)).start()
    print(f"connection with charging point {cp_id} opened")
    return make_response("200 OK", 200)


if __name__ == "__main__":
    # t = threading.Thread(target=interactive_connections.loop)
    # t.start()
    app.run(host="0.0.0.0", port=5000)
    # t.join()
