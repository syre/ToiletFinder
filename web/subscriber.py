#!/usr/bin/python3
import pika
import json

cred = pika.PlainCredentials("node", "node")
connection = pika.BlockingConnection(pika.ConnectionParameters("127.0.0.1",credentials=cred))

channel = connection.channel()
channel.exchange_declare(exchange="toilet_events", type="fanout")
result = channel.queue_declare()

queue_name = result.method.queue

channel.queue_bind(exchange="toilet_events", queue=queue_name)
def callback(ch, method, properties, body):
        print("%r" % json.loads(body.decode()))

channel.basic_consume(callback, queue=queue_name, no_ack=True)
channel.start_consuming()
connection.close()
