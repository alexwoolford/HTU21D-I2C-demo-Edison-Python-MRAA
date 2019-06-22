import io.woolford.differentschema.DifferentSchemaAvro;
import io.woolford.temperaturehumidity.SensorReadingV1;
import io.woolford.temperaturehumidity.SensorReadingV2;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class WriteSchemaTest {

//    kafka-avro-console-consumer --bootstrap-server cp53.woolford.io:9092 --topic temperature-humidity --property schema.registry.url="http://cp53.woolford.io:8081"

    @Test
    public void writeGoodSchemaV1Test(){

        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "cp53.woolford.io:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, io.confluent.kafka.serializers.KafkaAvroSerializer.class);
        props.put("schema.registry.url", "http://cp53.woolford.io:8081");

        KafkaProducer kafkaProducer = new KafkaProducer(props);

        SensorReadingV1 sensorReadingAvro = new SensorReadingV1();
        sensorReadingAvro.setHost("localhost");
        sensorReadingAvro.setTimestamp(new Date().getTime());
        sensorReadingAvro.setFahrenheit(72.0f);
        sensorReadingAvro.setHumidity(50.0f);

        ProducerRecord producerRecord = new ProducerRecord("temperature-humidity", sensorReadingAvro);

        kafkaProducer.send(producerRecord);
        kafkaProducer.flush();

    }

    @Test
    public void writeGoodSchemaV2Test(){

        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "cp53.woolford.io:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, io.confluent.kafka.serializers.KafkaAvroSerializer.class);
        props.put("schema.registry.url", "http://cp53.woolford.io:8081");

        KafkaProducer kafkaProducer = new KafkaProducer(props);

        SensorReadingV2 sensorReadingAvro = new SensorReadingV2();
        sensorReadingAvro.setHost("localhost");
        sensorReadingAvro.setTimestamp(new Date().getTime());
        sensorReadingAvro.setFahrenheit(72.0f);
        sensorReadingAvro.setHumidity(50.0f);
        sensorReadingAvro.setPressure(1000.0f);

        ProducerRecord producerRecord = new ProducerRecord("temperature-humidity", sensorReadingAvro);

        kafkaProducer.send(producerRecord);
        kafkaProducer.flush();

    }

    @Test(expected = SerializationException.class)
//    @Test
    public void writeBadSchemaTest(){


        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "cp53.woolford.io:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, io.confluent.kafka.serializers.KafkaAvroSerializer.class);
        props.put("schema.registry.url", "http://cp53.woolford.io:8081");

        KafkaProducer kafkaProducer = new KafkaProducer(props);

        DifferentSchemaAvro differentSchemaAvro = new DifferentSchemaAvro();
        differentSchemaAvro.setFirstname("Alex");
        differentSchemaAvro.setLastname("Woolford");

        ProducerRecord producerRecord = new ProducerRecord("temperature-humidity", differentSchemaAvro);

        kafkaProducer.send(producerRecord);
        kafkaProducer.flush();

    }


}
