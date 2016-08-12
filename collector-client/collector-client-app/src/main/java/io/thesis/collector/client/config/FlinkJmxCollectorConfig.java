package io.thesis.collector.client.config;

import io.thesis.collector.flink.jmx.FlinkJmxCollector;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.management.MBeanServerConnection;

@Profile("flink-jmx")
@Configuration
public class FlinkJmxCollectorConfig {

    @Bean
    FlinkJmxCollector jobManagerJmxCollector(final MBeanServerConnection mBeanServerConnection) {
        return new FlinkJmxCollector(mBeanServerConnection);
    }
}
