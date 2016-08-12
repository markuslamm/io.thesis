package io.thesis.collector.client.config;

import io.thesis.collector.dstat.DstatCollector;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("dstat")
@Configuration
public class DstatCollectorConfig {

    @Bean
    DstatCollector dstatCollector() {
        return new DstatCollector();
    }
}
