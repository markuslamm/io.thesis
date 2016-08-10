package io.thesis.collector.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * The main entry point of the collector-client application.
 *
 * Registers itself with Consul service discovery on startup and is thereby available for the {@code collector-manager}.
 */
@SpringBootApplication
@EnableDiscoveryClient
public class CollectorClientApp {

    /**
     * Starts the spring-boot application.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(CollectorClientApp.class, args);
    }
}
