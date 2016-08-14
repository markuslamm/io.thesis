package io.thesis.collector.commons;

/**
 * Available {@code Collector} implementations.
 */
public enum CollectorType {

    JVM_JMX,
    DSTAT,
    FLINK_REST,
    FLINK_JMX,
    KAFKA_BROKER_JMX
}
