package io.thesis.collector.flink;

import com.google.common.collect.Maps;
import io.thesis.collector.commons.AbstractCollector;
import io.thesis.collector.commons.CollectorResult;
import io.thesis.collector.commons.CollectorType;
import io.thesis.collector.commons.SampleCollector;
import io.thesis.collector.flink.rest.FlinkRestClient;
import io.thesis.collector.flink.rest.samples.ClusterInfoCollector;
import io.thesis.collector.flink.rest.samples.JobInfoCollector;

import java.util.Map;

import static java.lang.String.format;

/**
 * Collects data provided by Apache Flink's HTTP monitoring API. Uses an internal registry of
 * {@code SampleCollector}s and aggregates their results.
 */
public final class FlinkRestCollector extends AbstractCollector {

    public FlinkRestCollector(final FlinkRestClient flinkRestClient) {
        super(flinkRestSampleRegistry(flinkRestClient));
    }

    @Override
    public CollectorType getCollectorType() {
        return CollectorType.FLINK_REST;
    }


    @Override
    protected void checkRegistry() {
        if (getSampleRegistry().isEmpty()) {
            throw new FlinkRestCollectorException(format("No registered %s collectors", CollectorType.FLINK_REST));
        }
    }

    private static Map<String, SampleCollector> flinkRestSampleRegistry(final FlinkRestClient flinkRestClient) {
        final Map<String, SampleCollector> registry = Maps.newHashMap();
        registry.put(ClusterInfoCollector.SAMPLE_KEY, new ClusterInfoCollector(flinkRestClient));
        registry.put(JobInfoCollector.SAMPLE_KEY, new JobInfoCollector(flinkRestClient));
        return registry;
    }
}