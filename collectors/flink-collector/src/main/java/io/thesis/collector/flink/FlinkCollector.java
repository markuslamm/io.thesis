package io.thesis.collector.flink;

import com.google.common.collect.Maps;
import io.thesis.collector.commons.AbstractCollector;
import io.thesis.collector.commons.CollectorResult;
import io.thesis.collector.commons.CollectorType;
import io.thesis.collector.commons.SampleCollector;
import io.thesis.collector.flink.rest.FlinkRestClient;
import io.thesis.collector.flink.rest.samples.ClusterInfoCollector;
import io.thesis.collector.flink.rest.samples.JobInfoCollector;
import io.thesis.collector.jvm.samples.*;

import javax.management.MBeanServerConnection;
import java.util.Map;

import static java.lang.String.format;

/**
 * Collects data provided by Apache Flink's HTTP monitoring API and JMX interface. Uses an internal registry of
 * {@code SampleCollector}s and aggregates their results.
 */
public final class FlinkCollector extends AbstractCollector {

    public FlinkCollector(final FlinkRestClient flinkRestClient, final MBeanServerConnection mBeanServerConnection) {
        super(flinkSampleRegistry(flinkRestClient, mBeanServerConnection));
    }

    @Override
    public CollectorType getCollectorType() {
        return CollectorType.FLINK;
    }

    private static Map<String, SampleCollector> flinkSampleRegistry(final FlinkRestClient flinkRestClient,
                                                                    final MBeanServerConnection mBeanServerConnection) {
        final Map<String, SampleCollector> registry = Maps.newHashMap();
        registry.put(ClusterInfoCollector.SAMPLE_KEY, new ClusterInfoCollector(flinkRestClient));
        registry.put(JobInfoCollector.SAMPLE_KEY, new JobInfoCollector(flinkRestClient));
        registry.put(ClassloadingSampleCollector.SAMPLE_KEY, new ClassloadingSampleCollector(mBeanServerConnection));
        registry.put(BufferPoolSampleCollector.SAMPLE_KEY, new BufferPoolSampleCollector(mBeanServerConnection));
        registry.put(GcSampleCollector.SAMPLE_KEY, new GcSampleCollector(mBeanServerConnection));
        registry.put(MemorySampleCollector.SAMPLE_KEY, new MemorySampleCollector(mBeanServerConnection));
        registry.put(MemoryPoolSampleCollector.SAMPLE_KEY, new MemoryPoolSampleCollector(mBeanServerConnection));
        registry.put(OsSampleCollector.SAMPLE_KEY, new OsSampleCollector(mBeanServerConnection));
        registry.put(RuntimeSampleCollector.SAMPLE_KEY, new RuntimeSampleCollector(mBeanServerConnection));
        registry.put(ThreadSampleCollector.SAMPLE_KEY, new ThreadSampleCollector(mBeanServerConnection));
        return registry;
    }

    @Override
    protected void checkRegistry() {
        if (getSampleRegistry().isEmpty()) {
            throw new FlinkCollectorException(format("No registered %s collectors", CollectorType.FLINK));
        }
    }

    @Override
    protected CollectorResult createResult(final Map<String, Object> dataMap) {
        return new CollectorResult(CollectorType.FLINK.fullText(), dataMap);
    }
}