package io.thesis.collector.manager.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.thesis.collector.manager.discovery.CollectorClientInstanceService;
import io.thesis.collector.manager.discovery.MetadataRestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.client.RestTemplate;

import static java.util.Objects.requireNonNull;

/**
 * Spring configuration for {@code collector-manager} application. Creates required
 * spring beans and makes them available for dependency injection.
 */
@Configuration
public class CollectorManagerConfig {

    private final Environment env;

    @Autowired
    public CollectorManagerConfig(final Environment env) {
        this.env = requireNonNull(env);
    }

    /**
     * Creates a default RestTemplate.
     *
     * @return a {@code RestTemplate} bean available in the application context.
     */
    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }

    /**
     * Creates the client for fetching metadata from clients.
     *
     * @param restTemplate underlying {@code RestTemplate}
     * @return an {@code MetadataRestClient} bean available in the application context.
     */
    @Bean
    MetadataRestClient metadataRestClient(final RestTemplate restTemplate) {
        return new MetadataRestClient(restTemplate, env.getProperty("collector.client.metadata.path"));
    }

    @Bean
    CollectorClientInstanceService collectorClientInstanceService(final DiscoveryClient discoveryClient,
                                                                  final MetadataRestClient metadataRestClient) {
        return new CollectorClientInstanceService(discoveryClient, metadataRestClient,
                env.getProperty("collector.client.discovery.app-name"));
    }

    /**
     * Creates a customized {@code ObjectMapper} for JSON de-/serialization of new Java 8
     * Date and Time types, e.g. {@code LocalDateTime}.
     *
     * @return an {@code ObjectMapper} bean available in the application context.
     */
    @Bean
    ObjectMapper objectMapper() {
        final ObjectMapper bean = new ObjectMapper();
        bean.registerModule(new JavaTimeModule());
        bean.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        return bean;
    }
}
