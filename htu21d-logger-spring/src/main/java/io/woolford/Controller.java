package io.woolford;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;


@RestController
@EnableAutoConfiguration
public class Controller {

    private static final Logger logger = LoggerFactory.getLogger(Controller.class);
    private final String host = InetAddress.getLocalHost().getHostName();

    public Controller() throws UnknownHostException {
    }

    static {
        try {
            System.loadLibrary("mraajava");
        } catch (UnsatisfiedLinkError e) {
            logger.error(
                    "Native code library failed to load. See the chapter on Dynamic Linking Problems in the SWIG Java documentation for help.\n" +
                            e);
            System.exit(1);
        }
    }

    @RequestMapping("/get-temperature-humidity")
    SensorReading getTemperatureHumidity() throws IOException {
        SensorReader sensorReader = new SensorReader();
        return sensorReader.getTemperatureHumidity();
    }

}