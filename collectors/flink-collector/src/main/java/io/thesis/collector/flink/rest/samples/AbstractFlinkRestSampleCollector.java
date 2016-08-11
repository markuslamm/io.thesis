package io.thesis.collector.flink.rest.samples;

import io.thesis.collector.commons.SampleCollector;
import io.thesis.collector.flink.rest.FlinkRestClient;

import static java.util.Objects.requireNonNull;

/**
 * Base class for {@code RestSampleCollector} implementations. Provides access to
 * the underlying {@code FlinkRestClient}.
 */
abstract class AbstractFlinkRestSampleCollector implements SampleCollector {

    private final FlinkRestClient flinkRestClient;

    AbstractFlinkRestSampleCollector(final FlinkRestClient flinkRestClient) {
        this.flinkRestClient = requireNonNull(flinkRestClient);
    }

    protected FlinkRestClient restClient() {
        return flinkRestClient;
    }
}
