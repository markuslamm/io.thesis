package io.thesis.collector.client.config;

import io.thesis.collector.kafka.jmx.broker.KafkaBrokerJmxCollector;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.management.MBeanServerConnection;

@Profile("kafka-broker-jmx")
@Configuration
public class KafkaBrokerJmxConfig {

    @Bean
    KafkaBrokerJmxCollector kafkaNodeJmxCollector(final MBeanServerConnection mBeanServerConnection) {
        return new KafkaBrokerJmxCollector(mBeanServerConnection);
    }

}
