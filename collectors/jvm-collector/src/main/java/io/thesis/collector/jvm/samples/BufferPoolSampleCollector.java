package io.thesis.collector.jvm.samples;

import com.google.common.collect.Maps;
import io.thesis.collector.commons.jmx.AbstractJmxSampleCollector;
import io.thesis.collector.commons.jmx.JmxCollectorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import java.io.IOException;
import java.lang.management.BufferPoolMXBean;
import java.lang.management.ManagementFactory;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;

/**
 * Collects data from 'java.nio:type=BufferPool,name=*' MBean.
 */
public final class BufferPoolSampleCollector extends AbstractJmxSampleCollector {

    public static final String SAMPLE_KEY = "nio-buffer-pools";

    private static final Logger LOG = LoggerFactory.getLogger(BufferPoolSampleCollector.class);
    private static final String OBJECT_NAME = "java.nio:type=BufferPool,name=*";
    private static final String BUFFER_POOL_NAME_KEY = "name";
    private static final String BUFFER_POOL_COUNT_KEY = "count";
    private static final String BUFFER_POOL_MEM_USED_KEY = "memoryUsed";
    private static final String BUFFER_POOL_CAPACITY_KEY = "totalCapacity";

    public BufferPoolSampleCollector(final MBeanServerConnection mBeanServerConnection) {
        super(requireNonNull(mBeanServerConnection));
    }

    @Override
    public CompletableFuture<Map<String, Object>> collectSample() {
        return CompletableFuture.supplyAsync(() -> {
            final ObjectName queryName = convertStringToObjectName(OBJECT_NAME);
            final Map<String, Object> bufferPoolsResultMap = Maps.newLinkedHashMap();
            bufferPoolsResultMap.put(SAMPLE_KEY, parseBufferPools(queryObjectNames(queryName)));
            return bufferPoolsResultMap;
        });
    }

    private List<Map<String, Object>> parseBufferPools(final Set<ObjectName> objectNames) {
        return objectNames.stream()
                .map(objectName -> {
                    final Map<String, Object> bufferPoolDataMap = Maps.newLinkedHashMap();
                    final BufferPoolMXBean bufferPoolMXBean = getBufferPoolMXBean(objectName);
                    bufferPoolDataMap.put(BUFFER_POOL_NAME_KEY, bufferPoolMXBean.getName());
                    bufferPoolDataMap.put(BUFFER_POOL_COUNT_KEY, bufferPoolMXBean.getCount());
                    bufferPoolDataMap.put(BUFFER_POOL_MEM_USED_KEY, bufferPoolMXBean.getMemoryUsed());
                    bufferPoolDataMap.put(BUFFER_POOL_CAPACITY_KEY, bufferPoolMXBean.getTotalCapacity());
                    return bufferPoolDataMap;
                }).collect(Collectors.toList());
    }

    private BufferPoolMXBean getBufferPoolMXBean(final ObjectName objectName) {
        try {
            return ManagementFactory.newPlatformMXBeanProxy(mBeanServerConnection(), objectName.getCanonicalName(),
                    BufferPoolMXBean.class);
        } catch (IOException ex) {
            final JmxCollectorException exception = new JmxCollectorException(format("Unable to get %s: %s, cause: %s",
                    objectName.getCanonicalName(), ex.getMessage(), ex.getClass().getSimpleName()));
            LOG.warn(exception.getMessage());
            throw exception;
        }
    }
}
