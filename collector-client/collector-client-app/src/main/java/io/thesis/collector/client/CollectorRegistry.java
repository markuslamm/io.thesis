package io.thesis.collector.client;

import com.google.common.collect.Maps;
import io.thesis.collector.commons.Collector;
import io.thesis.collector.commons.CollectorType;

import java.util.Collection;
import java.util.Map;

/**
 * Registry component that holds instances of all registered {@code Collector}
 * implementations.
 */
public final class CollectorRegistry {

    private final Map<CollectorType, Collector> collectorMap;

    public CollectorRegistry() {
        collectorMap = Maps.newHashMap();
    }

    /**
     * Add a {@code Collector} to the registry.
     *
     * @param collector the {@code Collector} implementation to add
     */
    public void register(final Collector collector) {
        collectorMap.put(collector.getCollectorType(), collector);
    }

    /**
     * Get all registered collectors.
     *
     * @return collection of all {@code Collector} implementation instances
     */
    public Collection<Collector> getCollectors() {
        return collectorMap.values();
    }

    /**
     * Get all registered {@code CollectorType}s as strings.
     *
     * @return collection of {@code CollectorType}s
     */
    public Collection<CollectorType> getCollectorTypes() {
        return collectorMap.keySet();
    }
}
