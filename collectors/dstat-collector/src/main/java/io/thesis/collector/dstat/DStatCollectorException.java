package io.thesis.collector.dstat;

import io.thesis.collector.commons.CollectorException;

/**
 * Wraps exceptions that may arise while collecting dstat data.
 */
public final class DStatCollectorException extends CollectorException {

    private static final long serialVersionUID = 6001290867711204888L;

    public DStatCollectorException(final String message) {
        super(message);
    }
}
