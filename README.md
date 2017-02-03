# Python logger for the HTU21D temperature/humidity sensor

This logs temperature/humidity from an HTU21D sensor. It was tested on an Intel Edison board with the I2C breakout:

* <https://www.sparkfun.com/products/13024>
* <https://www.sparkfun.com/products/13034>

To interact with the board, I also used the base block:

* <https://www.sparkfun.com/products/13045>

![Edison with the HDU21D](edison_hdu21d.jpg)

OpenTSDB is great way to capture measure data from IoT devices. Here's a video example using the HTU21D:
[![Intel Edison logging to OpenTSDB](https://img.youtube.com/vi/PGFznFWoOZ4/0.jpg)](https://www.youtube.com/watch?v=PGFznFWoOZ4)
