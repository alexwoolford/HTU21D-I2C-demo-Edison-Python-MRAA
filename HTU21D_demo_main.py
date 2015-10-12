#!/usr/bin/python

import time
from HTU21D_Ed import HTU21D

# Initialise the HTU21D
htu1 = HTU21D(1, True)    # add <True> to enable all debug messages
htu6 = HTU21D(6, debug=True, freq=0)  # add <freq=0/1/2> to set I2C bus frequency.  Default=100kHz.

# Optional <True> debug enable may be sent to each HTU21D command
print "User Reg1: {}".format (hex(htu1.readUserReg()))
print "User Reg6: {}".format (hex(htu6.readUserReg()))

print "Change HTU1 resolution and SoftReset HTU6"
htu1.writeUserReg (0xc3)
htu6.softReset()  # This will clear the low battery signal for a short while
print "User Reg1: {}".format (hex(htu1.readUserReg()))
print "User Reg6: {}".format (hex(htu6.readUserReg()))

print "Returned error codes: CRC error: -255, I2C bus read error: -256"

for _ in range(3):

    temp1 = htu1.readTemperatureData()
    rh1 =   htu1.readRHData()
    temp6 = htu6.readTemperatureData()
    rh6 =   htu6.readRHData()

    print "Temperature1: %.2f C, RH1: %.2f %%" % (temp1, rh1)
    print "Temperature6: %.2f C, RH6: %.2f %%" % (temp6, rh6)

    time.sleep(1)
