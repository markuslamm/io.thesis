package io.thesis.collector.jvm.samples;

import com.google.common.collect.Maps;
import io.thesis.collector.commons.jmx.AbstractJmxSampleCollector;
import io.thesis.collector.commons.jmx.JmxCollectorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.MBeanServerConnection;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;

/**
 * Collects data from 'java.lang:type=Runtime' MBean.
 */
public final class RuntimeSampleCollector extends AbstractJmxSampleCollector {

    public static final String SAMPLE_KEY = "runtime";

    private static final Logger LOG = LoggerFactory.getLogger(RuntimeSampleCollector.class);
    private static final String OBJECT_NAME = "java.lang:type=Runtime";
    private static final String RUNTIME_SPEC_NAME_KEY = "specName";
    private static final String RUNTIME_SPEC_VENDOR_KEY = "specVendor";
    private static final String RUNTIME_SPEC_VERSION_KEY = "specVersion";
    private static final String RUNTIME_START_KEY = "startTime";
    private static final String RUNTIME_UPTIME_KEY = "uptime";
    private static final String RUNTIME_VM_NAME_KEY = "vmName";
    private static final String RUNTIME_VM_VENDOR_KEY = "vmVendor";
    private static final String RUNTIME_VM_VERSION_KEY = "vmVersion";

    public RuntimeSampleCollector(final MBeanServerConnection mBeanServerConnection) {
        super(requireNonNull(mBeanServerConnection));
    }

    @Override
    public CompletableFuture<Map<String, Object>> collectSample() {
        return CompletableFuture.supplyAsync(() -> {
            final RuntimeMXBean proxy = getRuntimeMXBean(OBJECT_NAME);
            final Map<String, Object> runtimeResultMap = Maps.newLinkedHashMap();
            runtimeResultMap.put(SAMPLE_KEY, parseRuntime(proxy));
            return runtimeResultMap;

        });
    }

    private Map<String, Object> parseRuntime(final RuntimeMXBean proxy) {
        final Map<String, Object> runtimeDataMap = Maps.newLinkedHashMap();
        runtimeDataMap.put(RUNTIME_START_KEY, proxy.getStartTime());
        runtimeDataMap.put(RUNTIME_UPTIME_KEY, proxy.getUptime());
        runtimeDataMap.put(RUNTIME_SPEC_NAME_KEY, proxy.getSpecName());
        runtimeDataMap.put(RUNTIME_SPEC_VENDOR_KEY, proxy.getSpecVendor());
        runtimeDataMap.put(RUNTIME_SPEC_VERSION_KEY, proxy.getSpecVersion());
        runtimeDataMap.put(RUNTIME_VM_NAME_KEY, proxy.getVmName());
        runtimeDataMap.put(RUNTIME_VM_VENDOR_KEY, proxy.getVmVendor());
        runtimeDataMap.put(RUNTIME_VM_VERSION_KEY, proxy.getVmVersion());
        return runtimeDataMap;
    }

    private RuntimeMXBean getRuntimeMXBean(final String objectName) {
        try {
            return ManagementFactory.newPlatformMXBeanProxy(mBeanServerConnection(), objectName,
                    RuntimeMXBean.class);
        } catch (IOException ex) {
            final JmxCollectorException exception = new JmxCollectorException(format("Unable to get %s: %s, cause: %s",
                    objectName, ex.getMessage(), ex.getClass().getSimpleName()));
            LOG.warn(exception.getMessage());
            throw exception;
        }
    }
}
