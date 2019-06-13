package io.woolford;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Component
@EnableKafka
public class TemperatureHumidityLogger {

    private static final Logger logger = LoggerFactory.getLogger(TemperatureHumidityLogger.class);
    private final String host = InetAddress.getLocalHost().getHostName();

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
    private void logTemperature() throws JsonProcessingException, UnknownHostException {

        SensorReader sensorReader = new SensorReader();

        ObjectMapper mapper = new ObjectMapper();
        String sensorReadingJson = mapper.writeValueAsString(sensorReader.getTemperatureHumidity());

        kafkaTemplate.send("temperature-humidity", sensorReadingJson);

    }

}
