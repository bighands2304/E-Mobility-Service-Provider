import time
import datetime
from flask import Flask, request, make_response
import websocket
import stomper
import json
import threading

TOPICS = ["BootNotification", "StatusNotification", "MeterValues", "StartTransaction", "StopTransaction",
          "ChangeAvailability", "ClearChargingProfile", "CancelReservation", "RemoteStartTransaction",
          "RemoteStopTransaction", "ReserveNow", "SetChargingProfile"]

MESSAGES_TO_SEND = ["StatusNotification", "MeterValues", "StartTransaction", "StopTransaction"]


class CpConnection:

    def __init__(self, cp_id, auth_key):
        self.session_id = None
        self.cp_id = cp_id
        self.websock = None
        self.auth_key = auth_key

    def create_connection(self):
        self.websock = websocket.WebSocketApp(f"ws://localhost:8080/ocpp?token={self.auth_key}",
                                              on_open=self.on_open,
                                              on_message=self.on_message,
                                              on_close=self.on_close)
        self.websock.run_forever()

    def on_open(self, ws: websocket.WebSocketApp):
        self.websock.send("CONNECT\naccept-version:1.0,1.1,2.0\n\n\x00\n")
        sub_msg = stomper.subscribe("/topic/ocpp/BootNotification", self.cp_id)
        self.websock.send(sub_msg)
        self.websock.send(stomper.send("/app/ocpp/BootNotification",
                                       json.dumps({"message": "Hi"}),
                                       content_type='application/json'))

    def on_message(self, ws, message):
        message_unpacked = stomper.unpack_frame(message)
        print("")
        print("==========================================================================================")
        print(f"message from cp: {self.cp_id} at destination {message_unpacked['headers']['destination']}")
        print(f"the message is: {message_unpacked['body']}")
        destination = message_unpacked["headers"]["destination"]
        if destination == "/topic/ocpp/BootNotification":
            self.on_boot_notification_conf(message_unpacked)
        elif destination in ["/topic/ocpp/ChangeAvailability", "/topic/ocpp/CancelReservation",
                             "/topic/ocpp/ReserveNow", "/topic/ocpp/SetChargingProfile",
                             "/topic/ocpp/ClearChargingProfile"]:
            request_id = message_unpacked["headers"]["requestId"]
            self.on_message_to_accept("/app/" + destination[7:] + "Conf", request_id)
        elif destination == "/topic/ocpp/RemoteStartTransaction":
            request_id = message_unpacked["headers"]["requestId"]
            self.on_remote_start_transaction(message_unpacked, request_id)
        elif destination == "/topic/ocpp/RemoteStopTransaction":
            request_id = message_unpacked["headers"]["requestId"]
            self.on_remote_stop_transaction(message_unpacked, request_id)
        print("==========================================================================================")
        print()

    def on_close(self, ws, a, b):
        print()
        print("==========================================================================================")
        print(f"Connection with {self.cp_id} closed")
        print("==========================================================================================")
        print()

    def close_connection(self):
        self.websock.close()

    def on_boot_notification_conf(self, message):
        self.session_id = message["body"]["sessionId"]
        for topic in TOPICS:
            sub_msg = stomper.subscribe(f"/topic/ocpp/{topic}", self.session_id)
            self.websock.send(sub_msg)

    def on_message_to_accept(self, topic, request_id):
        print("sending ACCEPTED as a response")
        res_message = {"commandResult": "ACCEPTED", "requestId": request_id}
        #headers = {"requestId": request_id}
        stomp_message = stomper.send(topic, res_message, content_type="application/json")
        self.websock.send(stomp_message)

    def on_remote_start_transaction(self, message, request_id):
        self.on_message_to_accept("/app/ocpp/RemoteStartTransactionConf", request_id)
        # todo: send some meter values, after some time send also stop transaction

    def on_remote_stop_transaction(self, message, request_id):
        self.on_message_to_accept("/app/ocpp/RemoteStartTransactionConf", request_id)
        # todo: after some time send stop transaction

    def send_msg(self, topic, message):
        stomp_message = stomper.send(topic, message, content_type="application/json")
        self.websock.send(stomp_message)


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
        self.connections[cp_id].close()

    def close_all(self):
        for cp_id in self.connections.keys():
            self.close_connection(cp_id)

    def loop(self):
        while True:
            print(len((self.connections.keys())))
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
    print(f"connection with charging point {cp_id} opened")
    return make_response("200 OK", 200)


if __name__ == "__main__":
    t = threading.Thread(target=interactive_connections.loop)
    t.start()
    app.run(port=3000)
    t.join()
