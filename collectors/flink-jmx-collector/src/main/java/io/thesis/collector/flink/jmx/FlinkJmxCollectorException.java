package io.thesis.collector.flink.jmx;

import io.thesis.collector.commons.jmx.JmxCollectorException;

class FlinkJmxCollectorException extends JmxCollectorException {

    private static final long serialVersionUID = -7450551536061145619L;

    FlinkJmxCollectorException(final String message) {
        super(message);
    }
}
