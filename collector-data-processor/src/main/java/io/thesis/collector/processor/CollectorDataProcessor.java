package io.thesis.collector.processor;

import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer09;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer09;
import org.apache.flink.streaming.util.serialization.SimpleStringSchema;

import java.util.Properties;

public class CollectorDataProcessor {

    private static final String KAFKA_HOST = "192.168.2.105:9092";
    private static final String INBOUND_TOPIC = "collector-outbound-topic";
    private static final String OUTBOUND_TOPIC = "flink-outbound-topic";

    public static void main(String[] args) throws Exception {

        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        final Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", KAFKA_HOST);

        final DataStream<String> kafkaInStream = env.addSource(new FlinkKafkaConsumer09<>(INBOUND_TOPIC,
                new SimpleStringSchema(), properties));

        /* this is the place to transform the collected data */

        kafkaInStream.print();


        kafkaInStream.addSink(new FlinkKafkaProducer09<>(KAFKA_HOST, OUTBOUND_TOPIC, new SimpleStringSchema()));
        env.execute();
    }
}
