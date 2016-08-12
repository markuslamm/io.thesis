package io.thesis.collector.flink;

import io.thesis.collector.commons.CollectorException;

public class FlinkCollectorException extends CollectorException {

    private static final long serialVersionUID = -7450551536061145619L;

    public FlinkCollectorException(final String message) {
        super(message);
    }

    public FlinkCollectorException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
