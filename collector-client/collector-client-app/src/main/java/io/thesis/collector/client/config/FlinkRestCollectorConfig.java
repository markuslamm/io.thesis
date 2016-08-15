package io.thesis.collector.client.config;

import io.thesis.collector.flink.rest.FlinkRestCollector;
import io.thesis.collector.flink.rest.client.FlinkRestClient;
import io.thesis.collector.flink.rest.client.FlinkRestClientImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.web.client.RestTemplate;

import static java.util.Objects.requireNonNull;

@Profile("flink-rest")
@Configuration
public class FlinkRestCollectorConfig {

    private final Environment env;

    @Autowired
    public FlinkRestCollectorConfig(final Environment env) {
        this.env = requireNonNull(env);
    }

    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    FlinkRestClient flinkRestClient(final RestTemplate restTemplate) {
        return new FlinkRestClientImpl(restTemplate, env.getProperty("collector.flink.rest.host"),
                env.getProperty("collector.flink.rest.port", Integer.class));
    }

    @Bean
    FlinkRestCollector flinkRestCollector(final FlinkRestClient flinkRestClient) {
        return new FlinkRestCollector(flinkRestClient);
    }
}
