import datetime
import json
import time

import numpy as np
import random
import string
import requests

CPMS_URL = "https://cpmsserver.up.railway.app/oscp/fp"
# CPMS_URL = "http://localhost:8080"
letters = string.ascii_lowercase
COMPANIES = ["Shell", "Enel", "Pure Power", "Green Power", "InnovaPower", "Airnergy", "AlternEnergy",
             "Geothermal Force"]
CONTENT_TYPE = "application/json"
ACCEPT_ENCODING = "gzip, deflate, br"


class OscpConnection:

    def __init__(self, cp_id):
        self.cp_id = cp_id
        self.token = ''.join(random.choice(letters) for _ in range(16))
        self.id = ''.join(random.choice(letters) for _ in range(8))
        self.capacity_mean = random.randint(50, 100)
        self.price_mean_0 = random.randint(50, 100) / 400
        self.price_mean_1 = random.randint(100, 200) / 400
        self.price_mean_2 = random.randint(75, 150) / 400
        if self.send_register() == 200:
            self.loop()

    def send_register(self):
        msg = {
            "token": self.token,
            "dsoId": self.id,
            "companyName": random.choice(COMPANIES),
            "cpId": self.cp_id,
            "url": "http://localhost:3000"
        }
        resp = requests.post(f"{CPMS_URL}/oscp/fp/register", json=msg,
                             headers={"Content-Type": CONTENT_TYPE,
                                      "X-Requested-With": "XMLHttpRequest",
                                      "Accept-Encoding": ACCEPT_ENCODING})
        print(f"""
        Connected to cp with id = {self.cp_id} with the oscp protocol
        The status code of the registration is {resp.status_code}
        """)
        return resp.status_code

    def loop(self):
        while True:
            try:
                end_time = datetime.datetime.now()
                start_time = end_time - datetime.timedelta(minutes=10)
                capacity = np.random.randn() * 10
                capacity_forecast = {
                    "id": self.cp_id,
                    "capacityForecastedType": "GENERATION",
                    "forecastedBlocks": [{
                        "capacity": self.capacity_mean + capacity,
                        "unit": "KWh",
                        "startTime": start_time.isoformat(),
                        "endTime": end_time.isoformat()
                    }]
                }
                requests.post(f"{CPMS_URL}/oscp/fp/update_group_capacity_forecast?token={self.token}",
                              json=capacity_forecast,
                              headers={"Content-Type": CONTENT_TYPE,
                                       "X-Requested-With": "XMLHttpRequest",
                                       "Accept-Encoding": ACCEPT_ENCODING})
                time.sleep(60)
                price = np.random.randn() / 10
                tou_msg = {
                    "timestamp": datetime.datetime.now().isoformat(),
                    "intervals": [
                        {
                            "startTime": "00:00:00.000",
                            "endTime": "07:00:00.000",
                            "unit": "W",
                            "price": self.price_mean_0 + price
                        },
                        {
                            "startTime": "07:00:00.000",
                            "endTime": "20:00:00.000",
                            "unit": "W",
                            "price": self.price_mean_1 + price
                        },
                        {
                            "startTime": "20:00:00.000",
                            "endTime": "23:59:59.999",
                            "unit": "W",
                            "price": self.price_mean_2 + price
                        }
                    ]
                }
                requests.post(f"{CPMS_URL}/openAdr/tou_pricing_event?token={self.token}&cpId={self.cp_id}",
                              json=tou_msg,
                              headers={"Content-Type": CONTENT_TYPE,
                                       "X-Requested-With": "XMLHttpRequest",
                                       "Accept-Encoding": ACCEPT_ENCODING})
            except requests.ConnectionError:
                print(f"Oscp connection with cp = {self.cp_id} is failed")
                return
            time.sleep(3600)
