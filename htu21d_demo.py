#!/usr/bin/python

import time
from htu21d import HTU21D

# Initialise the HTU21D
htu = HTU21D(1, False)    # add <True> to enable all debug messages

htu.writeUserReg(0xc3)
print "User Reg: {}".format(hex(htu.readUserReg()))

while True:

    temp = htu.readTemperatureData()
    rh = htu.readRHData()

    print "Temperature: %.2f C, RH: %.2f %%" % (temp, rh)

    time.sleep(0.1)
