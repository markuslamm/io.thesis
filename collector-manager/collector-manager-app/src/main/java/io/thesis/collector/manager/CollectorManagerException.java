package io.thesis.collector.manager;

/**
 * Wraps exceptions that arise in collector-manager.
 */
public class CollectorManagerException extends RuntimeException {
    
    private static final long serialVersionUID = 4031345278890768488L;

    public CollectorManagerException(final String msg, final Throwable cause) {
        super(msg, cause);
    }

    public CollectorManagerException(final String msg) {
        this(msg, null);
    }
}
