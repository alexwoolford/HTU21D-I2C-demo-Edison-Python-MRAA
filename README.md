# HTU21D-I2C-demo-Edison-Python-MRAA
HTU21D I2C demo for Edison in Python using MRAA and Mini Breakout Board

The HTU21D_Ed.py Python support library implements most of the sensor functions using the MRAA I2C functions, and runs on the Intel Edison Mini Breakout Board.  The library is derived from Adafruit and Sparkfun Python implementations.  MRAA I2C functions demonstrated include:

	• I2c bus instantiation with bus selection (raw=True)
	• readReg
	• writeReg
	• readBytesReg

HTU21D_demo_main.py demonstrates:

	• Reading and writing the user register on this sensor
	• Executing a Soft Reset
	• Getting the temperature and humidity bytes (and CRC check) from the sensor and translating them to degrees C and percent relative humidity, per the data sheet formulas.  Only sensor read Hold mode is implemented.
	• Debug messages can be enabled for the life of a sensor instantiation or on individual calls.  

Other useful references:

	• https://github.com/smoyerman/9dofBlock - implements I2C access using MRAA and runs on Edison.
	• https://github.com/sparkfun/SparkFun_HTU21D_Breakout_Arduino_Library - This demo for Arduino mode runs fine on the Edison Mini Breakout Board
	• https://github.com/mgaggero/Adafruit_Python_HTU21D by Massimo Gaggero implements working code for Raspberry Pi, which uses the Adafruit I2C libarary.  However…
	• https://github.com/adafruit/Adafruit-Raspberry-Pi-Python-Code - The Adafruit_I2C library uses the smbus library, which, while runs, does not properly configure the Edison pin function muxes, so all I2C accesses fail.  So this was a dead-end.  An interesting, but not-so-useful hack to get this Python demo to run is at 
	https://richardstechnotes.wordpress.com/2014/11/05/using-i2cdetect-on-the-intel-edison-and-getting-i2c6-to-work-on-the-mini-breakout-board/
	• Also see the MRAA documentation at http://iotdk.intel.com/docs/master/mraa/index.html.  

The bottom line…  To get this config to work (Edison, Python, HTU21D on I2C) use the MRAA library.

