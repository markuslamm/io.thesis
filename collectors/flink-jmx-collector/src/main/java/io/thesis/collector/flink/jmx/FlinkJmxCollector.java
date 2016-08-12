package io.thesis.collector.flink.jmx;

import com.google.common.collect.Maps;
import io.thesis.collector.commons.AbstractCollector;
import io.thesis.collector.commons.CollectorType;
import io.thesis.collector.commons.SampleCollector;
import io.thesis.collector.flink.jmx.jobmanager.JobManagerSampleCollector;

import javax.management.MBeanServerConnection;
import java.util.Map;

import static java.lang.String.format;

/**
 * Collects data for Apache Flink's new JMX JobManager metrics available since version 1.1.0.
 * Uses an internal registry of {@code SampleCollector}s and aggregates their results.
 */
class FlinkJmxCollector extends AbstractCollector {

    FlinkJmxCollector(final MBeanServerConnection mBeanServerConnection) {
        super(flinkSampleRegistry(mBeanServerConnection));
    }

    @Override
    public CollectorType getCollectorType() {
        return CollectorType.FLINK_JMX;
    }

    @Override
    protected void checkRegistry() {
        if(getSampleRegistry().isEmpty()) {
            throw new FlinkJmxCollectorException(format("No registered %s sample collectors", CollectorType.FLINK_JMX));
        }
    }

    private static  Map<String, SampleCollector> flinkSampleRegistry(final MBeanServerConnection mBeanServerConnection) {
        final Map<String, SampleCollector> registry = Maps.newHashMap();
        registry.put(JobManagerSampleCollector.SAMPLE_KEY, new JobManagerSampleCollector(mBeanServerConnection));
        return registry;
    }
}
