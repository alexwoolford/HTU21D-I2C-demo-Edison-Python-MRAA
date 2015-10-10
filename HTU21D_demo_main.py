#!/usr/bin/python

import time
from HTU21D_Ed import HTU21D

# Initialise the HTU21D
htu1 = HTU21D(1)  # add <True> to enable all debug messages
htu6 = HTU21D(6)

# Optional <True> debug enable may be sent to each HTU21D command
print "User Reg1: {}".format (hex(htu1.readUserReg()))
print "User Reg6: {}".format (hex(htu6.readUserReg()))

print "Change HTU1 resolution and SoftReset HTU6"
htu1.writeUserReg (0xc3)
htu6.softReset()  # This will clear the low battery signal for a short while
print "User Reg1: {}".format (hex(htu1.readUserReg()))
print "User Reg6: {}".format (hex(htu6.readUserReg()))


for _ in range(3):

    temp1 = htu1.readTemperatureData()
    rh1 =   htu1.readRHData()
    temp6 = htu6.readTemperatureData()
    rh6 =   htu6.readRHData()
        
    # Checking code not replicated for bus6
    if temp1 > -40 and rh1 > 0:
        print "Temperature1: %.2f C, RH1: %.2f %%" % (temp1, rh1)
        print "Temperature6: %.2f C, RH6: %.2f %%" % (temp6, rh6)
        print ""
    elif temp1 == -255:
        print "Temperature1 data CRC failed"
    elif rh1 == -255:
        print "RH1 data CRC failed"
    else:
        print "Invalid:" + str(temp1) + ", " + str(rh1)

    time.sleep(1)
