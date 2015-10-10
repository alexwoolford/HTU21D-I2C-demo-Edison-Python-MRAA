#!/usr/bin/env python

# HTU21D lib for Edison using mraa by Chris Nelson, October 2015
# Clues from Sparkfun and Adafruit implementations


import mraa as m
import time


class HTU21D:
    x = None

    # Commands
    TRIGGER_TEMP_MEASURE_HOLD = 0xE3
    TRIGGER_RH_MEASURE_HOLD =   0xE5
    READ_USER_REG =             0xE7
    WRITE_USER_REG =            0xE6
    SOFT_RESET =                0xFE

   # Constructor
    def __init__(self, bus, debug=False):
        self.x = m.I2c(bus, raw=True)  # raw=True forces manual bus selection, vs. board default
        self.debug = debug
        if self.debug:
            print self.x
        self.x.address(0x40) # Address of the HTU21D sensor



    def readUserReg(self, deb=False):
        "Read the user register byte"
        n = self.x.readReg(self.READ_USER_REG)
        if self.debug or deb:
            print "HTU21D Read User reg: {}".format (hex(n))
        return n


    def writeUserReg(self, value, deb=False):
        "Write the user register byte"
        n = self.x.writeReg(self.WRITE_USER_REG, value)
        if self.debug or deb:
            print "HTU21D Write User reg: {}, Status: {}, Read back: {}".format (hex(value), hex(n), hex(self.x.readReg(self.READ_USER_REG)))
        return n


    def softReset (self, deb=False):
        "Issue soft reset, and wait"
        n = self.x.writeReg(self.SOFT_RESET, 0)
        time.sleep (0.05)  # spec requires 15ms.  Set to 50ms, just because I'm not in that much of a hurry.
        if self.debug or deb:
            print "HTU21D Soft reset status: {}, Read back: {}".format (hex(n), hex(self.x.readReg(self.READ_USER_REG)))
        return n


    def readTemperatureData(self, deb=False):
        "Read 3 temperature bytes from the sensor"

        bytes3 = self.x.readBytesReg (self.TRIGGER_TEMP_MEASURE_HOLD, 3)
        if self.debug or deb:
            # print "Read temp returned: {0:x}, {0:b}".format (int(str(bytes3)))
            print "HTU21D Read temp returned:  {} {} {}".format (hex(bytes3[0]),hex(bytes3[1]),hex(bytes3[2]))
            #print "Read temp returned: {}".format (hex(bytes3))
        
        # If the readBytesReg call fails then the returned value is not a bytearray
        if not (type(bytes3) is bytearray):
            if self.debug or deb:
                print "HTU21D Read temp did not return 3 bytes"
            return -256  # readBytesReg failed
            
        # CRC Check
        if not self.crc8check(bytes3):
            if self.debug or deb:
                print "HTU21D Read temp CRC error"
            return -255  # bad CRC

        MSB = bytes3[0]
        LSB = bytes3[1]
        rawtemp = ((MSB <<8) | LSB) & 0xFFFC  # Lower two bits of LSB are status bits to be cleared
        if self.debug or deb:
            print "HTU21D Raw temp:            {0:x} hex, {0:d} decimal".format (rawtemp)

        # Apply datasheet forumula:  Temp = -46.85 + (175.72 * <sensor data> / 2^16
        temp = -46.85 + (175.72 * rawtemp / 2**16)
        if self.debug or deb:
            print "HTU21D Calculated temp:     {}".format (temp)

        return temp


    def readRHData(self, deb=False):
        "Read 3 relative humidity bytes from the sensor"

        bytes3 = self.x.readBytesReg (self.TRIGGER_RH_MEASURE_HOLD, 3)
        if self.debug or deb:
            # print "Read humidity returned: {0:x}, {0:b}".format (int(str(bytes3)))
            print "HTU21D Read RH returned:    {} {} {}".format (hex(bytes3[0]),hex(bytes3[1]),hex(bytes3[2]))

        # If the readBytesReg call fails then the returned value is not a bytearray
        if not (type(bytes3) is bytearray):
            if self.debug or deb:
                print "HTU21D Read RH did not return 3 bytes"
            return -256  # readBytesReg failed
            
        # CRC Check
        if not self.crc8check(bytes3):
            if self.debug or deb:
                print "HTU21D Read RH CRC error"
            return -255  # bad CRC

        MSB = bytes3[0]
        LSB = bytes3[1]
        rawRH = ((MSB <<8) | LSB) & 0xFFFC  # Lower two bits of LSB are status bits to be cleared
        if self.debug or deb:
            print "HTU21D Raw RH:              {0:x} hex, {0:d} decimal".format (rawRH)

        # Apply datasheet forumula:  RH = -6 + 125 * <sensor data> / 2^16
        RH = -6.0 + (125 * rawRH / 2**16)
        if self.debug or deb:
            print "HTU21D Calculated RH:       {}".format (RH)

        return RH


    def crc8check(self, value):
       "Calulate the CRC8 for the data received"
       # Ported from Sparkfun Arduino HTU21D Library:   https://github.com/sparkfun/HTU21D_Breakout
       remainder = ( ( value[0] << 8 ) + value[1] ) << 8
       remainder |= value[2]

       # POLYNOMIAL = 0x0131 = x^8 + x^5 + x^4 + 1
       # divsor = 0x988000 is the 0x0131 polynomial shifted to farthest left of three bytes
       divsor = 0x988000

       for i in range(0, 16):
           if( remainder & 1 << (23 - i) ):
               remainder ^= divsor

           divsor = divsor >> 1

       if remainder == 0:
           return True
       else:
           return False

