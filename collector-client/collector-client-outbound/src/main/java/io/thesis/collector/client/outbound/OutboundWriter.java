package io.thesis.collector.client.outbound;

/**
 * Base interface for components that write collected data into a sink.
 */
public interface OutboundWriter {

    /**
     * Writes JSON data.
     *
     * @param key the key the data is associated with
     * @param jsonData raw JSON data
     */
    void write(String key, String jsonData);
}
