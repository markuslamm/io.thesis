package io.thesis.collector.jvm.samples;

import com.google.common.collect.Maps;
import io.thesis.collector.commons.jmx.AbstractJmxSampleCollector;
import io.thesis.collector.commons.jmx.JmxCollectorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.MBeanServerConnection;
import java.io.IOException;
import java.lang.management.ClassLoadingMXBean;
import java.lang.management.ManagementFactory;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;

/**
 * Collects data from 'java.lang:type=ClassLoading' MBean.
 */
public final class ClassloadingSampleCollector extends AbstractJmxSampleCollector {

    public static final String SAMPLE_KEY = "classloading";

    private static final Logger LOG = LoggerFactory.getLogger(ClassloadingSampleCollector.class);
    private static final String OBJECT_NAME = "java.lang:type=ClassLoading";
    private static final String CLASSLOADING_LOADED_KEY = "loaded";
    private static final String CLASSLOADING_TOTAL_LOADED_KEY = "totalLoaded";
    private static final String CLASSLOADING_UNLOADED_KEY = "unloaded";

    public ClassloadingSampleCollector(final MBeanServerConnection mBeanServerConnection) {
        super(requireNonNull(mBeanServerConnection));
    }

    @Override
    public CompletableFuture<Map<String, Object>> collectSample() {
        return CompletableFuture.supplyAsync(() -> {
            final Map<String, Object> classloadingResultMap = Maps.newLinkedHashMap();
            classloadingResultMap.put(SAMPLE_KEY, parseClassloading(getClassLoadingMXBean(OBJECT_NAME)));
            return classloadingResultMap;
        });
    }

    private Map<String, Object> parseClassloading(final ClassLoadingMXBean proxy) {
        final Map<String, Object> classloadingDataMap = Maps.newLinkedHashMap();
        classloadingDataMap.put(CLASSLOADING_LOADED_KEY, proxy.getLoadedClassCount());
        classloadingDataMap.put(CLASSLOADING_TOTAL_LOADED_KEY, proxy.getTotalLoadedClassCount());
        classloadingDataMap.put(CLASSLOADING_UNLOADED_KEY, proxy.getUnloadedClassCount());
        return classloadingDataMap;
    }

    private ClassLoadingMXBean getClassLoadingMXBean(final String objectName) {
        try {
            return ManagementFactory.newPlatformMXBeanProxy(mBeanServerConnection(), objectName,
                    ClassLoadingMXBean.class);
        } catch (IOException ex) {
            final JmxCollectorException exception = new JmxCollectorException(format("Unable to get %s: %s, cause: %s",
                    objectName, ex.getMessage(), ex.getClass().getSimpleName()));
            LOG.warn(exception.getMessage());
            throw exception;
        }
    }
}
