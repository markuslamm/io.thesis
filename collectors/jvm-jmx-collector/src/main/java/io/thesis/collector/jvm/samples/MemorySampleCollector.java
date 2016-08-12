package io.thesis.collector.jvm.samples;

import com.google.common.collect.Maps;
import io.thesis.collector.commons.jmx.AbstractJmxSampleCollector;
import io.thesis.collector.commons.jmx.JmxCollectorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.MBeanServerConnection;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;

/**
 * Collects data from 'java.lang:type=Memory' MBean.
 */
public final class MemorySampleCollector extends AbstractJmxSampleCollector {

    public static final String SAMPLE_KEY = "memory";

    private static final Logger LOG = LoggerFactory.getLogger(MemorySampleCollector.class);
    private static final String OBJECT_NAME = "java.lang:type=Memory";
    private static final String MEMORY_OPFC_KEY = "objectPendingFinalizationCount";
    private static final String MEMORY_HEAP_USAGE_KEY = "heapMemoryUsage";
    private static final String MEMORY_NON_HEAP_USAGE_KEY = "nonHeapMemoryUsage";
    private static final String MEMORY_USAGE_COMMITTED_KEY = "committed";
    private static final String MEMORY_USAGE_INIT_KEY = "init";
    private static final String MEMORY_USAGE_MAX_KEY = "max";
    private static final String MEMORY_USAGE_USED_KEY = "used";

    public MemorySampleCollector(final MBeanServerConnection mBeanServerConnection) {
        super(requireNonNull(mBeanServerConnection));
    }

    @Override
    public CompletableFuture<Map<String, Object>> collectSample() {
        return CompletableFuture.supplyAsync(() -> {
            final Map<String, Object> memoryResultMap = Maps.newLinkedHashMap();
            memoryResultMap.put(SAMPLE_KEY, parseMemory(getMemoryMXBean(OBJECT_NAME)));
            return memoryResultMap;
        });
    }

    private Map<String, Object> parseMemory(final MemoryMXBean proxy) {
        final Map<String, Object> memoryDataMap = Maps.newLinkedHashMap();
        memoryDataMap.put(MEMORY_OPFC_KEY, proxy.getObjectPendingFinalizationCount());
        memoryDataMap.put(MEMORY_HEAP_USAGE_KEY, Optional.ofNullable(proxy.getHeapMemoryUsage())
                .map(MemorySampleCollector::parseMemoryUsage).orElse(null));
        memoryDataMap.put(MEMORY_NON_HEAP_USAGE_KEY, Optional.ofNullable(proxy.getNonHeapMemoryUsage())
                .map(MemorySampleCollector::parseMemoryUsage).orElse(null));
        return memoryDataMap;

    }

    private static Map<String, Object> parseMemoryUsage(final MemoryUsage memoryUsage) {
        final Map<String, Object> memoryUsageMap = Maps.newLinkedHashMap();
        memoryUsageMap.put(MEMORY_USAGE_COMMITTED_KEY, memoryUsage.getCommitted());
        memoryUsageMap.put(MEMORY_USAGE_INIT_KEY, memoryUsage.getInit());
        memoryUsageMap.put(MEMORY_USAGE_MAX_KEY, memoryUsage.getMax());
        memoryUsageMap.put(MEMORY_USAGE_USED_KEY, memoryUsage.getUsed());
        return memoryUsageMap;
    }

    private MemoryMXBean getMemoryMXBean(final String objectName) {
        try {
            return ManagementFactory.newPlatformMXBeanProxy(mBeanServerConnection(), objectName, MemoryMXBean.class);
        } catch (IOException ex) {
            final JmxCollectorException exception = new JmxCollectorException(format("Unable to get %s: %s, cause: %s",
                    objectName, ex.getMessage(), ex.getClass().getSimpleName()));
            LOG.warn(exception.getMessage());
            throw exception;
        }
    }
}
