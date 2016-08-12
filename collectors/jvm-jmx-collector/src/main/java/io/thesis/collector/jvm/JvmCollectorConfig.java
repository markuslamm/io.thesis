package io.thesis.collector.jvm;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.management.MBeanServerConnection;

@Profile("jvm-jmx")
@Configuration
public class JvmCollectorConfig {

    @Bean
    JvmCollector defaultJvmCollector(final MBeanServerConnection mBeanServerConnection) {
        return new JvmCollector(mBeanServerConnection);
    }
}
