package io.woolford.temperaturehumidity;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;


@RestController
@EnableAutoConfiguration
public class Controller {

    private static final Logger logger = LoggerFactory.getLogger(Controller.class);

    @RequestMapping("/get-temperature-humidity")
    SensorReading getTemperatureHumidity() throws IOException {
        SensorReader sensorReader = new SensorReader();
        return sensorReader.getSensorReading();
    }

}