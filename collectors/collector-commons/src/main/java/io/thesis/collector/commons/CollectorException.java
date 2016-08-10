package io.thesis.collector.commons;

/**
 * Wraps exceptions that arise in {@code Collector} implementations}.
 */
public class CollectorException extends RuntimeException {

    private static final long serialVersionUID = 5344025639481993268L;

    public CollectorException(final String message) {
        super(message);
    }

    public CollectorException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
