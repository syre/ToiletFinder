#!/usr/bin/python3

import mosquitto
import os

HOSTNAME = "127.0.0.1"
PORT = 1883


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

def on_message(mosq, obj, msg):
    print("Message received on topic "+msg.topic+" with QoS "+str(msg.qos)+" and payload "+msg.payload.decode())

pid = os.getpid()
mqttc = mosquitto.Mosquitto(str(pid))
mqttc.connect(HOSTNAME, PORT, 60, True)

mqttc.on_message = on_message
mqttc.on_connect = connect_callback
mqttc.on_disconnect = disconnect_callback
mqttc.subscribe("persist", 0)

while(mqttc.loop() == 0):
    pass

mqttc.disconnect()
