import datetime
import time

import numpy as np
import random
import string
import requests

CPMS_URL = "https://cpmsserver.up.railway.app/oscp/fp"
letters = string.ascii_lowercase
COMPANIES = ["Shell", "Enel", "Pure Power", "Green Power", "InnovaPower", "Airnergy", "AlternEnergy", "Geothermal Force"]


class OscpConnection:

    def __init__(self, cp_id):
        self.cp_id = cp_id
        self.token = ''.join(random.choice(letters) for _ in range(16))
        self.id = ''.join(random.choice(letters) for _ in range(8))
        self.capacity_mean = random.randint(50, 100)
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
        resp = requests.post(CPMS_URL + "/register", json=msg,
                             headers={"Content-Type": "application/json",
                                      "X-Requested-With": "XMLHttpRequest",
                                      "Accept-Encoding": "gzip, deflate, br"})
        print(f"""
        Connected to cp with id = {self.cp_id} with the oscp protocol
        The status code of the registration is {resp.status_code}
        """)
        return resp.status_code

    def loop(self):
        while True:
            time.sleep(600)
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
                        "startTime": start_time,
                        "endTime": end_time
                    }]
                }
                requests.post(CPMS_URL + "/update_group_capacity_forecast?token=" + self.token,
                              json=capacity_forecast,
                              headers={"Content-Type": "application/json",
                                       "X-Requested-With": "XMLHttpRequest",
                                       "Accept-Encoding": "gzip, deflate, br"})
            except requests.ConnectionError:
                print(f"Oscp connection with cp = {self.cp_id} is failed")
                return
