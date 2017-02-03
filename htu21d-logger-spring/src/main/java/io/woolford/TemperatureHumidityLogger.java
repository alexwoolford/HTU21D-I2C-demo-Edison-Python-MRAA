package io.woolford;

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class TemperatureHumidityLogger {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Scheduled(fixedDelay = 1000L)
    private void logTermperature() throws IOException, I2CFactory.UnsupportedBusNumberException {

        I2CBus i2cBus = I2CFactory.getInstance(I2CBus.BUS_1);

        I2CDevice tempSensor = i2cBus.getDevice(0x18);

        byte[] buffer = new byte[2];

        int amountBytes = tempSensor.read(5, buffer, 0, 2);

        System.out.println("Amount of byte read : " + amountBytes);
        for(byte b : buffer) {
            System.out.println("Data : "+ b +" hex 0x"+Integer.toHexString((b & 0xff)));
        }

    }


}
