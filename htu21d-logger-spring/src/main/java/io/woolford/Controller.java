package io.woolford;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import upm_htu21d.javaupm_htu21dConstants;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

@RestController
@EnableAutoConfiguration
public class Controller {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final String host = InetAddress.getLocalHost().getHostName();

    public Controller() throws UnknownHostException {
    }

    static {
        try {
            System.loadLibrary("mraajava");
        } catch (UnsatisfiedLinkError e) {
            System.err.println(
                    "Native code library failed to load. See the chapter on Dynamic Linking Problems in the SWIG Java documentation for help.\n" +
                            e);
            System.exit(1);
        }
    }

    @RequestMapping("/get-temperature-humidity")
    SensorReading getTemperatureHumidity() throws IOException {

        upm_htu21d.HTU21D sensor = new upm_htu21d.HTU21D(1, javaupm_htu21dConstants.HTU21D_I2C_ADDRESS);
        sensor.sampleData();

        double celsius = sensor.getTemperature();
        double fahrenheit = (9.0/5.0) * celsius + 32;

        SensorReading sensorReading = new SensorReading();
        sensorReading.setHost(host);
        sensorReading.setTimestamp(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX")
                .withZone(ZoneOffset.UTC)
                .format(Instant.now()));
        sensorReading.setFahrenheit(fahrenheit);
        sensorReading.setHumidity(sensor.getHumidity());

        return sensorReading;
    }

}