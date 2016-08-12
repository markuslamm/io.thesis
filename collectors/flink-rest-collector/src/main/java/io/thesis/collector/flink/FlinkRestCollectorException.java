package io.thesis.collector.flink;

import io.thesis.collector.commons.CollectorException;

class FlinkRestCollectorException extends CollectorException {

    private static final long serialVersionUID = -7450551536061145619L;

    FlinkRestCollectorException(final String message) {
        super(message);
    }
}
