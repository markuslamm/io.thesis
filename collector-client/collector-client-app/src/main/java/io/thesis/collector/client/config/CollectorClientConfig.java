package io.thesis.collector.client.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.thesis.collector.client.CollectorClient;
import io.thesis.collector.client.CollectorRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import static java.util.Objects.requireNonNull;

@Configuration
public class CollectorClientConfig {

    private static final Logger LOG = LoggerFactory.getLogger(CollectorClientConfig.class);

    private final Environment env;
    private final ApplicationContext context;

    @Autowired
    public CollectorClientConfig(final Environment env, final ApplicationContext context) {
        this.env = requireNonNull(env);
        this.context = requireNonNull(context);
    }

    @Bean
    CollectorRegistry collectorRegistry() {
        final CollectorRegistry registry = new CollectorRegistry();
        return registry;
    }

    @Bean
    CollectorClient collectorClient(final CollectorRegistry collectorRegistry) {
        return new CollectorClient(collectorRegistry, env.getProperty("spring.cloud.consul.discovery.instanceId", "unknown"),
                env.getProperty("server.port", Integer.class));
    }

    @Bean
    LocalValidatorFactoryBean localValidatorFactoryBean() {
        return new LocalValidatorFactoryBean();
    }

    @Bean
    ObjectMapper objectMapper() {
        final ObjectMapper bean = new ObjectMapper();
        bean.registerModule(new JavaTimeModule());
        bean.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        return bean;
    }
}
