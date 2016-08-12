package io.thesis.collector.commons;

import static java.util.Objects.requireNonNull;

/**
 * Available {@code Collector} implementations.
 */
public enum CollectorType {

    JVM_JMX("jvm-jmx"),
    DSTAT("dstat"),
    FLINK("flink-1.1.0"),
    KAFKA("kafka-0.9.0.1");

    private final String fullText;

    CollectorType(final String fullText) {
        this.fullText = requireNonNull(fullText);
    }

    public String fullText() {
        return fullText;
    }

    public static CollectorType of(final String text) {
        final CollectorType result;
        switch (text) {
            case "jvm-jmx":
                result = JVM_JMX;
                break;
            case "dstat":
                result = DSTAT;
                break;
            case "flink-1.1.0":
                result = FLINK;
                break;
            case "kafka-0.9.0.1":
                result = KAFKA;
                break;
            default:
                throw new CollectorException(String.format("Unknown source type: %s", text));
        }
        return result;
    }
}
