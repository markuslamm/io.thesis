package io.thesis.collector.commons;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Base interface for components that collect data from JMX-MBeans.
 */
public interface SampleCollector {

    /**
     * Collects data from JMX-MBeans.
     *
     * @return a future with collected data
     */
    CompletableFuture<Map<String, Object>> collectSample();
}
