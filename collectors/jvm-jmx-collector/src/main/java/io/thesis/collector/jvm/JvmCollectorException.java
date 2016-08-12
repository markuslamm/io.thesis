package io.thesis.collector.jvm;

import io.thesis.collector.commons.jmx.JmxCollectorException;

public class JvmCollectorException extends JmxCollectorException {

    public JvmCollectorException(final String message) {
        super(message);
    }

    public JvmCollectorException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
