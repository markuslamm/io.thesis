package io.thesis.collector.client.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.management.MBeanServerConnection;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import java.io.IOException;

import static java.util.Objects.requireNonNull;

@Profile({ "jvm-jmx", "flink-jmx"})
@Configuration
public class MBeanConnectionConfig {

    private final String url;

    public MBeanConnectionConfig(@Value("${collector.jmx.url}") final String url) {
        this.url =requireNonNull(url);
    }

    @Bean
    MBeanServerConnection mBeanServerConnection() throws IOException {
        final JMXServiceURL jmxServiceURL = new JMXServiceURL(url);
        final JMXConnector jmxConnector = JMXConnectorFactory.connect(jmxServiceURL, null);
        return jmxConnector.getMBeanServerConnection();
    }
}
