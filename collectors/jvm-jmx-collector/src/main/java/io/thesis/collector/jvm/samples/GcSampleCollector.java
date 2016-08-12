package io.thesis.collector.jvm.samples;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.thesis.collector.commons.jmx.AbstractJmxSampleCollector;
import io.thesis.collector.commons.jmx.JmxCollectorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.InstanceNotFoundException;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import java.io.IOException;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
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
 * Collects data from 'java.lang:type=GarbageCollector,name=*' MBean.
 */
public final class GcSampleCollector extends AbstractJmxSampleCollector {

    public static final String SAMPLE_KEY = "garbage-collectors";

    private static final Logger LOG = LoggerFactory.getLogger(GcSampleCollector.class);
    private static final String OBJECT_NAME = "java.lang:type=GarbageCollector,name=*";
    private static final String GC_NAME_KEY = "name";
    private static final String GC_COLLECTION_COUNT_KEY = "collectionCount";
    private static final String GC_COLLECTION_TIME_KEY = "collectionTime";
    private static final String GC_LASTGC_KEY = "lastGcInfo";
    private static final String GC_MEM_POOLS_KEY = "memoryPoolNames";
    private static final String GC_VALID_KEY = "valid";
    private static final String GC_LASTGC_STARTTIME_KEY = "starttime";
    private static final String GC_LASTGC_ENDTIME_KEY = "endtime";
    private static final String GC_LASTGC_DURATION_KEY = "duration";
    private static final String GC_LASTGC_MEM_BEFORE_KEY = "memoryUsageBeforeGc";
    private static final String GC_LASTGC_MEM_AFTER_KEY = "memoryUsageAfterGc";
    private static final String GC_MEM_COMMITTED_KEY = "committed";
    private static final String GC_MEM_INIT_KEY = "init";
    private static final String GC_MEM_MAX_KEY = "max";
    private static final String GC_MEM_USED_KEY = "used";

    public GcSampleCollector(final MBeanServerConnection mBeanServerConnection) {
        super(requireNonNull(mBeanServerConnection));
    }

    @Override
    public CompletableFuture<Map<String, Object>> collectSample() {
        return CompletableFuture.supplyAsync(() -> {
            final ObjectName queryName = convertStringToObjectName(OBJECT_NAME);
            final Set<ObjectName> objectNames = queryObjectNames(queryName);
            final Map<String, Object> gcsResultMap = Maps.newLinkedHashMap();
            gcsResultMap.put(SAMPLE_KEY, parseGarbageCollectors(objectNames));
            return gcsResultMap;
        });
    }

    private List<Map<String, Object>> parseGarbageCollectors(final Set<ObjectName> objectNames) {
        return objectNames.stream()
                .map(this::parseGarbageCollector)
                .collect(Collectors.toList());
    }

    private Map<String, Object> parseGarbageCollector(final ObjectName objectName) {
        try {
            final Map<String, Object> gcDataMap = Maps.newLinkedHashMap();
            final GarbageCollectorMXBean defaultGcMXBean =
                    ManagementFactory.newPlatformMXBeanProxy(mBeanServerConnection(),
                            objectName.getCanonicalName(), GarbageCollectorMXBean.class);

            gcDataMap.put(GC_NAME_KEY, defaultGcMXBean.getName());
            gcDataMap.put(GC_COLLECTION_COUNT_KEY, defaultGcMXBean.getCollectionCount());
            gcDataMap.put(GC_COLLECTION_TIME_KEY, defaultGcMXBean.getCollectionTime());
            gcDataMap.put(GC_MEM_POOLS_KEY, defaultGcMXBean.getMemoryPoolNames());
            gcDataMap.put(GC_VALID_KEY, defaultGcMXBean.isValid());

            if (isSunMxBean(objectName)) {
                com.sun.management.GarbageCollectorMXBean sunGcMXBean =
                        ManagementFactory.newPlatformMXBeanProxy(mBeanServerConnection(),
                                objectName.getCanonicalName(), com.sun.management.GarbageCollectorMXBean.class);

                final Map<String, Object> lastGcInfoMap = Optional.ofNullable(sunGcMXBean.getLastGcInfo())
                        .map(gcInfo -> {
                            final Map<String, Object> map = Maps.newLinkedHashMap();
                            map.put(GC_LASTGC_STARTTIME_KEY, gcInfo.getStartTime());
                            map.put(GC_LASTGC_ENDTIME_KEY, gcInfo.getEndTime());
                            map.put(GC_LASTGC_DURATION_KEY, gcInfo.getDuration());
                            final List<Map<String, Object>> memBeforeGc = Optional.ofNullable(gcInfo.getMemoryUsageBeforeGc())
                                    .map(GcSampleCollector::parseMemoryUsage).orElse(null);
                            final List<Map<String, Object>> memAfterGc = Optional.ofNullable(gcInfo.getMemoryUsageAfterGc())
                                    .map(GcSampleCollector::parseMemoryUsage).orElse(null);
                            map.put(GC_LASTGC_MEM_BEFORE_KEY, memBeforeGc);
                            map.put(GC_LASTGC_MEM_AFTER_KEY, memAfterGc);
                            return map;
                        }).orElse(null);
                gcDataMap.put(GC_LASTGC_KEY, lastGcInfoMap);
            }
            return gcDataMap;
        } catch (IOException ex) {
            final JmxCollectorException exception = new JmxCollectorException(format("Unable to collect %s: %s, cause: %s",
                    objectName.getCanonicalName(), ex.getMessage(), ex.getClass().getSimpleName()));
            LOG.warn(exception.getMessage());
            throw exception;
        }
    }

    private static List<Map<String, Object>> parseMemoryUsage(final Map<String, MemoryUsage> memoryUsages) {
        final List<Map<String, Object>> memoryUsagesLists = Lists.newArrayList();
        memoryUsages.forEach((memoryName, memoryUsage) -> {
            final Map<String, Object> memoryUsageMap = Maps.newLinkedHashMap();
            memoryUsageMap.put(GC_NAME_KEY, memoryName);
            memoryUsageMap.put(GC_MEM_COMMITTED_KEY, memoryUsage.getCommitted());
            memoryUsageMap.put(GC_MEM_INIT_KEY, memoryUsage.getInit());
            memoryUsageMap.put(GC_MEM_MAX_KEY, memoryUsage.getMax());
            memoryUsageMap.put(GC_MEM_USED_KEY, memoryUsage.getUsed());
            memoryUsagesLists.add(memoryUsageMap);
        });
        return memoryUsagesLists;
    }

    private boolean isSunMxBean(final ObjectName objectName) {
        try {
            return mBeanServerConnection().isInstanceOf(objectName, "com.sun.management.GarbageCollectorMXBean");
        } catch (InstanceNotFoundException | IOException ex) {
            final JmxCollectorException exception = new JmxCollectorException(format("Unable to collect GcMBeanSample: %s, "
                    + "cause: %s", ex.getMessage(), ex.getClass().getSimpleName()));
            LOG.warn(exception.getMessage());
            throw exception;
        }
    }
}
