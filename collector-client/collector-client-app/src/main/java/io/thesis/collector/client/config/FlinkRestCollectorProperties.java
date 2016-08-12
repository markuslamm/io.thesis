package io.thesis.collector.client.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "collector.flink.rest", locations = "classpath:flink-rest-collector-config.yml")
public final class FlinkRestCollectorProperties {

    private String host;
    private int port;

    public String getHost() {
        return host;
    }

    public void setHost(final String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(final int port) {
        this.port = port;
    }
}
