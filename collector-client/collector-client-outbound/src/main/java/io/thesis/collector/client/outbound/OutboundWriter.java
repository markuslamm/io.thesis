package io.thesis.collector.client.outbound;

/**
 * Base interface for components the write collected data into a sink.
 */
public interface OutboundWriter {

    void write(String jsonData);
}
