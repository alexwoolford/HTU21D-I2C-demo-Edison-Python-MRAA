package io.woolford.temperaturehumidity;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.net.UnknownHostException;

import static java.util.Collections.emptyList;

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

        // get measurement
        SensorReader sensorReader = new SensorReader();
        SensorReading sensorReading = sensorReader.getSensorReading();

        Metrics.counter("measurements").increment();

        // create Avro sensor reading object
        SensorReadingV1 sensorReadingAvro = SensorReadingV1.newBuilder()
                .setHost(sensorReading.getHost())
                .setTimestamp(sensorReading.getTimestamp())
                .setFahrenheit(sensorReading.getFahrenheit())
                .setHumidity(sensorReading.getHumidity())
                .build();

        // send Avro message to Kafka
        kafkaTemplate.send("temperature-humidity", sensorReadingAvro);

    }

}
