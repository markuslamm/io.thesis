package io.thesis.collector.commons;

import static java.util.Objects.requireNonNull;

/**
 * Available {@code Collector} implementations.
 */
public enum CollectorType {

    JVM_JMX,
    DSTAT,
    FLINK_REST,
    FLINK_JMX,
//    FLINK_JMX_JOBMANAGER,
//    FLINK_JMX_TASKMANAGER,
    KAFKA_SERVER
}
