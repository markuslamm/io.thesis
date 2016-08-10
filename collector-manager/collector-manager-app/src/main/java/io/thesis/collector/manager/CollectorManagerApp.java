package io.thesis.collector.manager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * The main entry point of the collector-manager application.
 * Registers itself with Consul service discovery on startup.
 */
@SpringBootApplication
@EnableDiscoveryClient
public class CollectorManagerApp {

    /**
     * Starts the spring-boot application.
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(CollectorManagerApp.class, args);
    }
}
