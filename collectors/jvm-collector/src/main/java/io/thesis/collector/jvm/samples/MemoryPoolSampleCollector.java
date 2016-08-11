package io.thesis.collector.jvm.samples;

import com.google.common.collect.Maps;
import io.thesis.collector.commons.jmx.AbstractJmxSampleCollector;
import io.thesis.collector.commons.jmx.JmxCollectorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.MemoryUsage;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;

/**
 * Collects data from 'java.lang:type=MemoryPool,name=*' MBean.
 */
public final class MemoryPoolSampleCollector extends AbstractJmxSampleCollector {

    public static final String SAMPLE_KEY = "memoryPools";

    private static final Logger LOG = LoggerFactory.getLogger(MemoryPoolSampleCollector.class);
    private static final String OBJECT_NAME = "java.lang:type=MemoryPool,name=*";
    private static final String MEMORY_POOL_COL_USAGE_KEY = "collectionUsage";
    private static final String MEMORY_POOL_COL_USAGE_THRESHOLD_KEY = "collectionThreshold";
    private static final String MEMORY_POOL_COL_USAGE_THRESHOLD_COUNT_KEY = "collectionCount";
    private static final String MEMORY_POOL_COL_USAGE_THRESHOLD_EXC_KEY = "collectionExceeded";
    private static final String MEMORY_POOL_MEM_MANAGERS_KEY = "memoryManagerNames";
    private static final String MEMORY_POOL_NAME_KEY = "name";
    private static final String MEMORY_POOL_PEAK_USAGE = "peakUsage";
    private static final String MEMORY_POOL_TYPE = "type";
    private static final String MEMORY_POOL_USAGE_KEY = "usage";
    private static final String MEMORY_POOL_USAGE_THRESHOLD_KEY = "usageThreshold";
    private static final String MEMORY_POOL_USAGE_THRESHOLD_COUNT_KEY = "usageCount";
    private static final String MEMORY_POOL_USAGE_THRESHOLD_EXC_KEY = "usageExceeded";
    private static final String MEMORY_POOL_VALID = "valid";
    private static final String MEMORY_POOL_COMMITTED_KEY = "committed";
    private static final String MEMORY_POOL_INIT_KEY = "init";
    private static final String MEMORY_POOL_MAX_KEY = "max";
    private static final String MEMORY_POOL_USED_KEY = "used";


    public MemoryPoolSampleCollector(final MBeanServerConnection mBeanServerConnection) {
        super(requireNonNull(mBeanServerConnection));
    }

    @Override
    public CompletableFuture<Map<String, Object>> collectSample() {
        return CompletableFuture.supplyAsync(() -> {
            final ObjectName queryName = convertStringToObjectName(OBJECT_NAME);
            final Set<ObjectName> objectNames = queryObjectNames(queryName);
            final Map<String, Object> memoryPoolResultMap = Maps.newLinkedHashMap();
            memoryPoolResultMap.put(SAMPLE_KEY, parseMemoryPools(objectNames));
            return memoryPoolResultMap;
        });
    }

    private List<Map<String, Object>> parseMemoryPools(final Set<ObjectName> objectNames) {
        return objectNames.stream()
                .map(this::parseMemoryPool)
                .collect(Collectors.toList());
    }

    private Map<String, Object> parseMemoryPool(final ObjectName objectName) {
        final MemoryPoolMXBean memoryPoolMXBean = getMemoryPoolMXBean(objectName);
        final Map<String, Object> memoryPoolDataMap = Maps.newLinkedHashMap();
        memoryPoolDataMap.put(MEMORY_POOL_NAME_KEY, memoryPoolMXBean.getName());
        memoryPoolDataMap.put(MEMORY_POOL_TYPE, memoryPoolMXBean.getType());
        memoryPoolDataMap.put(MEMORY_POOL_VALID, memoryPoolMXBean.isValid());
        final Map<String, Object> collectionUsage = Optional.ofNullable(memoryPoolMXBean.getCollectionUsage())
                .map(this::parseMemoryUsage).orElse(null);
        final Map<String, Object> usage = Optional.ofNullable(memoryPoolMXBean.getUsage())
                .map(this::parseMemoryUsage).orElse(null);
        final Map<String, Object> peakUsage = Optional.ofNullable(memoryPoolMXBean.getPeakUsage())
                .map(this::parseMemoryUsage).orElse(null);
        memoryPoolDataMap.put(MEMORY_POOL_COL_USAGE_KEY, collectionUsage);
        memoryPoolDataMap.put(MEMORY_POOL_USAGE_KEY, usage);
        memoryPoolDataMap.put(MEMORY_POOL_PEAK_USAGE, peakUsage);
        if (memoryPoolMXBean.isCollectionUsageThresholdSupported()) {
            memoryPoolDataMap.put(MEMORY_POOL_COL_USAGE_THRESHOLD_KEY, memoryPoolMXBean.getCollectionUsageThreshold());
            memoryPoolDataMap.put(MEMORY_POOL_COL_USAGE_THRESHOLD_COUNT_KEY, memoryPoolMXBean.getCollectionUsageThresholdCount());
            memoryPoolDataMap.put(MEMORY_POOL_COL_USAGE_THRESHOLD_EXC_KEY, memoryPoolMXBean.isCollectionUsageThresholdExceeded());
        }
        memoryPoolDataMap.put(MEMORY_POOL_MEM_MANAGERS_KEY, memoryPoolMXBean.getMemoryManagerNames());

        if (memoryPoolMXBean.isUsageThresholdSupported()) {
            memoryPoolDataMap.put(MEMORY_POOL_USAGE_THRESHOLD_KEY, memoryPoolMXBean.getUsageThreshold());
            memoryPoolDataMap.put(MEMORY_POOL_USAGE_THRESHOLD_COUNT_KEY, memoryPoolMXBean.getUsageThresholdCount());
            memoryPoolDataMap.put(MEMORY_POOL_USAGE_THRESHOLD_EXC_KEY, memoryPoolMXBean.isUsageThresholdExceeded());
        }
        return memoryPoolDataMap;
    }

    private Map<String, Object> parseMemoryUsage(final MemoryUsage memoryUsage) {
        final Map<String, Object> memoryUsageMap = Maps.newLinkedHashMap();
        memoryUsageMap.put(MEMORY_POOL_COMMITTED_KEY, memoryUsage.getCommitted());
        memoryUsageMap.put(MEMORY_POOL_INIT_KEY, memoryUsage.getInit());
        memoryUsageMap.put(MEMORY_POOL_MAX_KEY, memoryUsage.getMax());
        memoryUsageMap.put(MEMORY_POOL_USED_KEY, memoryUsage.getUsed());
        return memoryUsageMap;
    }

    private MemoryPoolMXBean getMemoryPoolMXBean(final ObjectName objectName) {
        try {
            return ManagementFactory.newPlatformMXBeanProxy(mBeanServerConnection(), objectName.getCanonicalName(),
                    MemoryPoolMXBean.class);
        } catch (IOException ex) {
            final JmxCollectorException exception = new JmxCollectorException(format("Unable to get %s: %s, cause: %s",
                    objectName.getCanonicalName(), ex.getMessage(), ex.getClass().getSimpleName()));
            LOG.warn(exception.getMessage());
            throw exception;
        }
    }
}
