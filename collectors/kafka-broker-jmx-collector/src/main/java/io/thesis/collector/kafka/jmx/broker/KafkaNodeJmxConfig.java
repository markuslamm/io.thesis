package io.thesis.collector.kafka.jmx.broker;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.management.MBeanServerConnection;

@Profile("kafka-node-jmx")
@Configuration
public class KafkaNodeJmxConfig {

    @Bean
    KafkaBrokerJmxCollector kafkaNodeJmxCollector(final MBeanServerConnection mBeanServerConnection) {
        return new KafkaBrokerJmxCollector(mBeanServerConnection);
    }



}
