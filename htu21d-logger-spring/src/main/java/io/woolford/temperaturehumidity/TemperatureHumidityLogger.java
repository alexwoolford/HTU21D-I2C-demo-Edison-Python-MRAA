package io.woolford.temperaturehumidity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.net.UnknownHostException;

@Component
@EnableKafka
public class TemperatureHumidityLogger {

    private static final Logger logger = LoggerFactory.getLogger(TemperatureHumidityLogger.class);

    @Autowired
    private KafkaTemplate kafkaTemplate;

    public TemperatureHumidityLogger() throws UnknownHostException {
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

    @Scheduled(cron = "*/2 * * * * *") // run every 2 seconds
    private void logTemperature() throws UnknownHostException {

        SensorReader sensorReader = new SensorReader();

        SensorReading sensorReading = sensorReader.getSensorReading();

        SensorReadingAvro sensorReadingAvro = SensorReadingAvro.newBuilder()
                .setHost(sensorReading.getHost())
                .setTimestamp((int) System.currentTimeMillis())
                .setFahrenheit(sensorReading.getFahrenheit())
                .setHumidity(sensorReading.getHumidity())
                .build();

        kafkaTemplate.send("temperature-humidity", sensorReadingAvro);

    }

}
