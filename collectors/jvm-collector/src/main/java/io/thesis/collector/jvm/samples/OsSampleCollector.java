package io.thesis.collector.jvm.samples;

import com.google.common.collect.Maps;
import io.thesis.collector.commons.jmx.AbstractJmxSampleCollector;
import io.thesis.collector.commons.jmx.JmxCollectorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.InstanceNotFoundException;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;

/**
 * Collects data from 'java.lang:type=OperatingSystem' MBean.
 */
public final class OsSampleCollector extends AbstractJmxSampleCollector {

    public static final String SAMPLE_KEY = "operating-system";

    private static final Logger LOG = LoggerFactory.getLogger(OsSampleCollector.class);
    private static final String OBJECT_NAME = "java.lang:type=OperatingSystem";
    private static final String OS_ARCH_KEY = "arch";
    private static final String OS_PROCS_KEY = "availableProcessors";
    private static final String OS_NAME_KEY = "name";
    private static final String OS_SYS_LOAD_AVG_KEY = "systemLoadAvg";
    private static final String OS_VERSION_KEY = "version";
    private static final String OS_PROCESS_CPU_LOAD_KEY = "processCpuLoad";
    private static final String OS_PROCESS_CPU_TIME_KEY = "processCpuTime";
    private static final String OS_COMMITTED_VIRTUAL_MEM_SIZE_KEY = "committedVirtualMemorySize";
    private static final String OS_SYSTEM_CPU_LOAD_KEY = "systemCpuLoad";
    private static final String OS_TOTAL_PHYSICAL_MEM_SIZE_KEY = "totalPhysicalMemorySize";
    private static final String OS_FREE_PHYSICAL_MEM_SIZE_KEY = "freePhysicalMemorySize";
    private static final String OS_TOTAL_SWAP_SIZE_KEY = "totalSwapSpaceSize";
    private static final String OS_FREE_SWAP_SIZE_KEY = "freeSwapSpaceSize";
    private static final String OS_MAX_FILE_DESC_COUNT_KEY = "maxFileDescriptorCount";
    private static final String OS_OPEN_FILE_DESC_COUNT_KEY = "openFileDescriptorCount";
    private static final String SUN_CLASS_NAME = "com.sun.management.OperatingSystemMXBean";
    private static final String SUN_UNIX_CLASS_NAME = "com.sun.management.OperatingSystemMXBean";

    public OsSampleCollector(MBeanServerConnection mBeanServerConnection) {
        super(requireNonNull(mBeanServerConnection));
    }

    @Override
    public CompletableFuture<Map<String, Object>> collectSample() {
        return CompletableFuture.supplyAsync(() -> {
            final ObjectName objectName = convertStringToObjectName(OBJECT_NAME);
            final Map<String, Object> osResultMap = Maps.newLinkedHashMap();
            osResultMap.put(SAMPLE_KEY, parseOs(objectName));
            return osResultMap;
        });
    }

    private Map<String, Object> parseOs(final ObjectName objectName) {
        try {
            final Map<String, Object> osDataMap = Maps.newLinkedHashMap();
            //default os bean
            final OperatingSystemMXBean defaultOsMXBean =
                        ManagementFactory.newPlatformMXBeanProxy(mBeanServerConnection(),
                                objectName.getCanonicalName(), OperatingSystemMXBean.class);
            osDataMap.put(OS_ARCH_KEY, defaultOsMXBean.getArch());
            osDataMap.put(OS_PROCS_KEY, defaultOsMXBean.getAvailableProcessors());
            osDataMap.put(OS_NAME_KEY, defaultOsMXBean.getName());
            osDataMap.put(OS_SYS_LOAD_AVG_KEY, defaultOsMXBean.getSystemLoadAverage());
            osDataMap.put(OS_VERSION_KEY, defaultOsMXBean.getVersion());

            //sun os bean
            if (isMxBeanInstanceOf(objectName, SUN_CLASS_NAME)) {
                final com.sun.management.OperatingSystemMXBean osMXBean =
                        ManagementFactory.newPlatformMXBeanProxy(mBeanServerConnection(),
                                objectName.getCanonicalName(), com.sun.management.OperatingSystemMXBean.class);
                osDataMap.put(OS_PROCESS_CPU_LOAD_KEY, osMXBean.getProcessCpuLoad());
                osDataMap.put(OS_PROCESS_CPU_TIME_KEY, osMXBean.getProcessCpuTime());
                osDataMap.put(OS_COMMITTED_VIRTUAL_MEM_SIZE_KEY, osMXBean.getCommittedVirtualMemorySize());
                osDataMap.put(OS_SYSTEM_CPU_LOAD_KEY, osMXBean.getSystemCpuLoad());
                osDataMap.put(OS_TOTAL_PHYSICAL_MEM_SIZE_KEY, osMXBean.getTotalPhysicalMemorySize());
                osDataMap.put(OS_FREE_PHYSICAL_MEM_SIZE_KEY, osMXBean.getFreePhysicalMemorySize());
                osDataMap.put(OS_TOTAL_SWAP_SIZE_KEY, osMXBean.getTotalSwapSpaceSize());
                osDataMap.put(OS_FREE_SWAP_SIZE_KEY, osMXBean.getFreeSwapSpaceSize());
            }
            //sun unix os bean
            if (isMxBeanInstanceOf(objectName, SUN_UNIX_CLASS_NAME)) {
                final com.sun.management.UnixOperatingSystemMXBean osMXBean =
                        ManagementFactory.newPlatformMXBeanProxy(mBeanServerConnection(),
                                objectName.getCanonicalName(), com.sun.management.UnixOperatingSystemMXBean.class);
                osDataMap.put(OS_MAX_FILE_DESC_COUNT_KEY, osMXBean.getMaxFileDescriptorCount());
                osDataMap.put(OS_OPEN_FILE_DESC_COUNT_KEY, osMXBean.getOpenFileDescriptorCount());
            }
            return osDataMap;
        } catch (IOException ex) {
            final JmxCollectorException exception = new JmxCollectorException(format("Unable to collect %s: %s, cause: %s",
                    objectName, ex.getMessage(), ex.getClass().getSimpleName()));
            LOG.warn(exception.getMessage());
            throw exception;
        }
    }

    private boolean isMxBeanInstanceOf(final ObjectName objectName, final String type) {
        try {
            return mBeanServerConnection().isInstanceOf(objectName, type);
        } catch (InstanceNotFoundException | IOException ex) {
            final JmxCollectorException exception = new JmxCollectorException(format("Unable to collect %s: %s, cause: %s",
                    objectName, ex.getMessage(), ex.getClass().getSimpleName()));
            LOG.warn(exception.getMessage());
            throw exception;
        }
    }
}
