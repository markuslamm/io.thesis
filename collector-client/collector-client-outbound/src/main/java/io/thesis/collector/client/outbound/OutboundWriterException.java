package io.thesis.collector.client.outbound;

/**
 * Wraps exceptions that arise in {@code OutboundWriter}.
 */
public class OutboundWriterException extends RuntimeException {

    private static final long serialVersionUID = 7073999864615001222L;

    public OutboundWriterException(final String msg) {
        super(msg);
    }
}
