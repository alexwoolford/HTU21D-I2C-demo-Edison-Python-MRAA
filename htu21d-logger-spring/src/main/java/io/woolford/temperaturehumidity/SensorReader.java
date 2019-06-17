package io.woolford.temperaturehumidity;

import upm_htu21d.javaupm_htu21dConstants;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;

class SensorReader {

    SensorReading getSensorReading() throws UnknownHostException {

        upm_htu21d.HTU21D sensor = new upm_htu21d.HTU21D(1, javaupm_htu21dConstants.HTU21D_I2C_ADDRESS);
        sensor.sampleData();

        String host = InetAddress.getLocalHost().getHostName();

        float celsius = sensor.getTemperature();
        float fahrenheit = ((9.0f/5.0f) * celsius + 32.0f);

        SensorReading sensorReading = new SensorReading();
        sensorReading.setHost(host);
        sensorReading.setTimestamp(new Date());
        sensorReading.setFahrenheit(fahrenheit);
        sensorReading.setHumidity(sensor.getHumidity());

        return sensorReading;

    }

}
