package io.woolford;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import upm_htu21d.javaupm_htu21dConstants;

@Component
public class TemperatureHumidityLogger {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());


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

    @Scheduled(cron = "*/10 * * * * *") // run every 10 seconds
    private void logTermperature() throws InterruptedException {

        upm_htu21d.HTU21D sensor = new upm_htu21d.HTU21D(1, javaupm_htu21dConstants.HTU21D_I2C_ADDRESS);
        sensor.sampleData();

        SensorReading sensorReading = new SensorReading();
        sensorReading.setTemperature(sensor.getTemperature());
        sensorReading.setHumidity(sensor.getHumidity());
        sensorReading.setTimestamp(System.currentTimeMillis() / 1000);

        logger.info(sensorReading.toString());
        //TODO: convert to avro
        //TODO: write Avro to Kafka topic

    }

}
