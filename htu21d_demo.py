#!/usr/bin/env python

from htu21d import HTU21D
import socket
import time
import json
from kafka import KafkaProducer


class TemperatureKafkaLogger:

    """
        Log temperature, as JSON, to a Kafka topic.
    """

    def __init__(self):
        # Initialise the HTU21D
        self.htu = HTU21D(1, False)    # add <True> to enable all debug messages

        self.htu.writeUserReg(0xc3)
        # print "User Reg: {}".format(hex(self.htu.readUserReg()))

        self.host = socket.gethostname()

        self.producer = KafkaProducer(bootstrap_servers='hadoop02:6667')

    def run(self):
        while True:

            # capture temp, relative humidity, and timestamp
            celcius = self.htu.readTemperatureData()
            rh = self.htu.readRHData()
            epoch_time = time.time()

            record = {'host': self.host, 'ts': epoch_time, 'celcius': celcius, 'rh': rh}

            # write to Kafka topic
            self.producer.send('temperature', json.dumps(record))

            time.sleep(0.1)

if __name__ == "__main__":
    temperature_kafka_logger = TemperatureKafkaLogger()
    temperature_kafka_logger.run()
