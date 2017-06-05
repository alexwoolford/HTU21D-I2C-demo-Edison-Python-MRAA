package io.woolford;

import com.twitter.bijection.Injection;
import com.twitter.bijection.avro.GenericAvroCodecs;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.reflect.ReflectData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import upm_htu21d.javaupm_htu21dConstants;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Component
public class TemperatureHumidityLogger {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final String host = InetAddress.getLocalHost().getHostName();

    @Autowired
    private KafkaTemplate kafkaTemplate;

    public TemperatureHumidityLogger() throws UnknownHostException {
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

    @Scheduled(cron = "*/10 * * * * *") // run every 10 seconds
    private void logTermperature() throws InterruptedException {

        upm_htu21d.HTU21D sensor = new upm_htu21d.HTU21D(1, javaupm_htu21dConstants.HTU21D_I2C_ADDRESS);
        sensor.sampleData();

        double celsius = sensor.getTemperature();
        double fahrenheit = (9.0/5.0) * celsius + 32;

        SensorReading sensorReading = new SensorReading();
        sensorReading.setHost(host);
        sensorReading.setTimestamp(System.currentTimeMillis() / 1000);
        sensorReading.setFahrenheit(fahrenheit);
        sensorReading.setHumidity(sensor.getHumidity());

        Schema schema = ReflectData.get().getSchema(SensorReading.class);
        GenericData.Record record = new GenericData.Record(schema);
        record.put("host", sensorReading.getHost());
        record.put("timestamp", sensorReading.getTimestamp());
        record.put("fahrenheit", fahrenheit);
        record.put("humidity", sensorReading.getHumidity());

        Injection<GenericRecord, byte[]> recordInjection = GenericAvroCodecs.toBinary(schema);
        byte[] recordBytes = recordInjection.apply(record);

        kafkaTemplate.send("temperature_humidity", recordBytes);

    }

}
