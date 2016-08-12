package io.thesis.collector.client.config;

import io.thesis.collector.flink.FlinkRestCollector;
import io.thesis.collector.flink.rest.FlinkRestClient;
import io.thesis.collector.flink.rest.FlinkRestClientImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.client.RestTemplate;

import static java.util.Objects.requireNonNull;

@Profile("flink-rest")
@Configuration
@EnableConfigurationProperties(FlinkRestCollectorProperties.class)
public class FlinkRestCollectorConfig {

    private final FlinkRestCollectorProperties flinkRestCollectorProperties;

    @Autowired
    public FlinkRestCollectorConfig(final FlinkRestCollectorProperties flinkRestCollectorProperties) {
        this.flinkRestCollectorProperties = requireNonNull(flinkRestCollectorProperties);
    }

    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    FlinkRestClient flinkRestClient(final RestTemplate restTemplate) {
        return new FlinkRestClientImpl(restTemplate, flinkRestCollectorProperties.getHost(), flinkRestCollectorProperties.getPort());
    }

    @Bean
    FlinkRestCollector flinkRestCollector(final FlinkRestClient flinkRestClient) {
        return new FlinkRestCollector(flinkRestClient);
    }
}
