package io.thesis.collector.commons;

import java.util.concurrent.CompletableFuture;

/**
 * The main interface for all collectors that fetches data from a certain data source.
 */
public interface Collector {

    /**
     * Collects data from data source.
     *
     * @return a future with a {@code CollectorResult} containing the collected data.
     */
    CompletableFuture<CollectorResult> collect();

    /**
     * Get the {@code CollectorType}.
     *
     * @return the type of collector
     */
    CollectorType getCollectorType();
}
