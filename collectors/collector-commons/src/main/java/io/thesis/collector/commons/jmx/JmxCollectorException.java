package io.thesis.collector.commons.jmx;

import io.thesis.collector.commons.CollectorException;

/**
 * Encapsulates exceptions that may occur while collecting JMX data.
 */
public class JmxCollectorException extends CollectorException {

    private static final long serialVersionUID = 512431282101179439L;

    public JmxCollectorException(final String message) {
        super(message);
    }

    public JmxCollectorException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
