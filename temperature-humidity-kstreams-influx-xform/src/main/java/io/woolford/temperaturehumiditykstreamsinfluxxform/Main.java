package io.woolford.temperaturehumiditykstreamsinfluxxform;

import io.confluent.kafka.serializers.AbstractKafkaAvroSerDeConfig;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import io.woolford.temperaturehumidity.SensorReadingInfluxV1;
import io.woolford.temperaturehumidity.SensorReadingV1;
import io.woolford.temperaturehumidity.TagsRecord;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.*;
import org.apache.kafka.streams.kstream.KStream;

import java.util.Properties;

public class Main {

    public static void main(String[] args) {

        // set props for Kafka Steams app (see KafkaConstants)
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, KafkaConstants.APPLICATION_ID);
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaConstants.KAFKA_BROKERS);
        props.put(AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, KafkaConstants.SCHEMA_REGISTRY_URL);
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, SpecificAvroSerde.class);
        props.put(StreamsConfig.PRODUCER_PREFIX + ProducerConfig.INTERCEPTOR_CLASSES_CONFIG, "io.confluent.monitoring.clients.interceptor.MonitoringProducerInterceptor");
        props.put(StreamsConfig.CONSUMER_PREFIX + ConsumerConfig.INTERCEPTOR_CLASSES_CONFIG, "io.confluent.monitoring.clients.interceptor.MonitoringConsumerInterceptor");

        final StreamsBuilder builder = new StreamsBuilder();

        // create a stream of the raw iris records
        KStream<String, SensorReadingV1> temperatureHumidityStream = builder.stream("temperature-humidity");

        temperatureHumidityStream.map((key, value) -> {

            TagsRecord tagsRecord = new TagsRecord();
            tagsRecord.setHost(value.getHost());

            SensorReadingInfluxV1 sensorReadingInfluxAvro = new SensorReadingInfluxV1();
            sensorReadingInfluxAvro.setTags(tagsRecord);

            sensorReadingInfluxAvro.setMeasurement("temperature-humidity");
            sensorReadingInfluxAvro.setFahrenheit(value.getFahrenheit());
            sensorReadingInfluxAvro.setHumidity(value.getHumidity());

            return new KeyValue<>(key, sensorReadingInfluxAvro);
        }).to("temperature-humidity-influx");

        // run it
        final Topology topology = builder.build();
        final KafkaStreams streams = new KafkaStreams(topology, props);
        streams.start();

        // Add shutdown hook to respond to SIGTERM and gracefully close Kafka Streams
        Runtime.getRuntime().addShutdownHook(new Thread(streams::close));

    }

}
