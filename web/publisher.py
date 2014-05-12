#!/usr/bin/python3

import mosquitto
import os
import time
import datetime
import json

HOSTNAME = "127.0.0.1"
PORT = 1883

def publish_callback(mosq, obj, mid):
    print("Message published")


def connect_callback(mosq, obj, rc):
    if rc == 0:
        print("connected to broker")
    else:
        print("could not connect to broker, error code: "+ str(rc))

def disconnect_callback(mosq, obj, rc):
    if rc == 0:
        print("disconnected from broker")
    else:
        print("disconnected from broker, error code: "+str(rc))

pid = os.getpid()
mqttc = mosquitto.Mosquitto(str(pid))
mqttc.on_connect = connect_callback
mqttc.on_publish = publish_callback
mqttc.on_disconnect = disconnect_callback

mqttc.connect(HOSTNAME, PORT, 60, True)

message = {"group_id":1, "toilet_id": 2, "occupied":False, "methane_level": 100, "time_stamp": str(datetime.datetime.now())}
jsonstring = json.dumps(message)

while(mqttc.loop() == 0):
    time.sleep(1)
    mqttc.publish("persist", jsonstring, 2)

mqttc.disconnect()
