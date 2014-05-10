#!/usr/bin/python3

import mosquitto
import os
import time
import datetime
import json

HOSTNAME = "127.0.0.1"
PORT = 1883

pid = os.getpid()

mqttc = mosquitto.Mosquitto(str(pid))
mqttc.connect(HOSTNAME, PORT, 60, True)

message = {"group_id":1, "toilet_id": 2, "occupied":False, "methane_level": 100, "time_stamp": str(datetime.datetime.now())}
jsonstring = json.dumps(message)

while(mqttc.loop() == 0):
    time.sleep(1)
    mqttc.publish("persist", jsonstring, 0)

mqttc.disconnect()
