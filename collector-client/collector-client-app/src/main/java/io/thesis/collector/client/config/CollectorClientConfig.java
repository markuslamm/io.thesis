package io.thesis.collector.client.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.thesis.collector.client.CollectorClient;
import io.thesis.collector.client.CollectorRegistry;
import io.thesis.collector.client.outbound.KafkaOutboundWriter;
import io.thesis.collector.client.outbound.OutboundWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.Objects.requireNonNull;

@Configuration
public class CollectorClientConfig {

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
    CollectorClient collectorClient(final CollectorRegistry collectorRegistry, final OutboundWriter outboundWriter,
                                    final TaskScheduler taskScheduler) {
        return new CollectorClient(collectorRegistry, outboundWriter, taskScheduler,
                env.getProperty("spring.cloud.consul.discovery.instanceId", "unknown"),
                env.getProperty("server.port", Integer.class),
                env.getProperty("collector.client.default-interval-in-ms", Integer.class));
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

    @Bean
    TaskScheduler taskScheduler() {
        final ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setPoolSize(3); //TODO check
        threadPoolTaskScheduler.setThreadFactory(new DaemonThreadFactory("collector-thread"));
        threadPoolTaskScheduler.setRemoveOnCancelPolicy(true);
        return threadPoolTaskScheduler;
    }

    @Bean
    OutboundWriter outboundWriter(final KafkaTemplate<String, String> kafkaTemplate) {
        return new KafkaOutboundWriter(kafkaTemplate, env.getProperty("kafka.outbound-topic"),
                env.getProperty("kafka.outbound-key"));
    }

    private static class DaemonThreadFactory implements ThreadFactory {
        private final String threadNamePrefix;
        private final AtomicInteger counter = new AtomicInteger(0);

        DaemonThreadFactory(final String threadNamePrefix) {
            this.threadNamePrefix = threadNamePrefix;
        }

        @Override
        public Thread newThread(final Runnable r) {
            Thread t = new Thread(r, threadNamePrefix + "-" + counter.addAndGet(1));
            t.setDaemon(true);
            return t;
        }
    }
}
