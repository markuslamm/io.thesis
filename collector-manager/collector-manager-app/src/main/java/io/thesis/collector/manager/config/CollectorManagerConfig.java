package io.thesis.collector.manager.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Spring configuration for {@code collector-manager} application. Creates required
 * spring beans and makes them available for dependency injection.
 */
@Configuration
public class CollectorManagerConfig {

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
