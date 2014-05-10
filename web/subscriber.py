#!/usr/bin/python3

import mosquitto
import os

HOSTNAME = "127.0.0.1"
PORT = 1883

pid = os.getpid()
mqttc = mosquitto.Mosquitto(str(pid))
mqttc.connect(HOSTNAME, PORT, 60, True)

def on_message(mosq, obj, msg):
    print("Message received on topic "+msg.topic+" with QoS "+str(msg.qos)+" and payload "+msg.payload.decode())


mqttc.on_message = on_message
mqttc.subscribe("persist", 0)

while(mqttc.loop() == 0):
    pass

mqttc.disconnect()
