package io.woolford;

import upm_htu21d.javaupm_htu21dConstants;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;

public class SensorReader {

    public SensorReading getTemperatureHumidity() throws UnknownHostException {

        upm_htu21d.HTU21D sensor = new upm_htu21d.HTU21D(1, javaupm_htu21dConstants.HTU21D_I2C_ADDRESS);
        sensor.sampleData();

        String host = InetAddress.getLocalHost().getHostName();

        double celsius = sensor.getTemperature();
        double fahrenheit = (9.0/5.0) * celsius + 32;

        SensorReading sensorReading = new SensorReading();
        sensorReading.setHost(host);
        sensorReading.setTimestamp(new Date());
        sensorReading.setFahrenheit(fahrenheit);
        sensorReading.setHumidity(sensor.getHumidity());

        return sensorReading;

    }

}
