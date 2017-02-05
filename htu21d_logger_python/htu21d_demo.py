#!/usr/bin/env python

from htu21d import HTU21D
import time
import potsdb


class TemperatureHumidityLogger:

    """
        Log temperature and relative humidity, and write to OpenTSDB
    """

    def __init__(self):
        # Initialise the HTU21D
        self.htu = HTU21D(1, False)    # add <True> to enable all debug messages

        self.metrics = potsdb.Client(host='10.0.1.11', port=4242, qsize=0, host_tag=True, mps=0, check_host=True)

    def run(self):
        while True:

            # capture temp, relative humidity
            celcius = self.htu.readTemperatureData()
            rh = self.htu.readRHData()

            print "celcius: " + str(celcius) + "; relative humidity: " + str(rh)
            # # sent to OpenTSDB
            self.metrics.send('celcius', celcius)
            self.metrics.send('rh', rh)

            time.sleep(0.1)

if __name__ == "__main__":
    temperature_humidity_logger = TemperatureHumidityLogger()
    temperature_humidity_logger.run()
