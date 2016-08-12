package io.thesis.collector.jvm;//package io.thesis.collector.jvm;

import com.google.common.collect.Maps;
import io.thesis.collector.commons.AbstractCollector;
import io.thesis.collector.commons.CollectorResult;
import io.thesis.collector.commons.CollectorType;
import io.thesis.collector.commons.SampleCollector;
import io.thesis.collector.commons.jmx.JmxCollectorException;
import io.thesis.collector.jvm.samples.*;

import javax.management.MBeanServerConnection;
import java.util.Map;

import static java.lang.String.format;

/**
 * Collects default JVM data via the JMX interface. Uses an internal registry of {@code SampleCollector}s
 * and aggregates their results.
 */
final class JvmCollector extends AbstractCollector {

    JvmCollector(final MBeanServerConnection mBeanServerConnection) {
        super(jvmSampleRegistry(mBeanServerConnection));
    }

    @Override
    public CollectorType getCollectorType() {
        return CollectorType.JVM_JMX;
    }

    @Override
    protected void checkRegistry() {
        if (getSampleRegistry().isEmpty()) {
            throw new JmxCollectorException(format("No registered %s sample collectors", CollectorType.JVM_JMX));
        }
    }

    private static Map<String, SampleCollector> jvmSampleRegistry(final MBeanServerConnection mBeanServerConnection) {
        final Map<String, SampleCollector> registry = Maps.newHashMap();
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
}
