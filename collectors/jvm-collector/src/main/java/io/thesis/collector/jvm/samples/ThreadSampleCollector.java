package io.thesis.collector.jvm.samples;

import com.google.common.collect.Maps;
import io.thesis.collector.commons.jmx.AbstractJmxSampleCollector;
import io.thesis.collector.commons.jmx.JmxCollectorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.MBeanServerConnection;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;

/**
 * Collects data from 'java.lang:type=Threading' MBean.
 */
public final class ThreadSampleCollector extends AbstractJmxSampleCollector {

    public static final String SAMPLE_KEY = "threads";

    private static final Logger LOG = LoggerFactory.getLogger(ThreadSampleCollector.class);
    private static final String OBJECT_NAME = "java.lang:type=Threading";
    private static final String THREAD_COUNT_KEY = "threadCount";
    private static final String THREAD_DAEMON_COUNT_KEY = "daemonThreadCount";
    private static final String THREAD_PEAK_COUNT_KEY = "peakThreadCount";
    private static final String THREAD_CURRENT_USER_TIME_KEY = "currentThreadUserTime";
    private static final String THREAD_CURRENT_CPU_TIME_KEY = "currentThreadCpuTime";
    private static final String THREAD_TOTAL_STARTED_KEY = "totalStartedThreadCount";

    private static final String THREAD_DETAILS_KEY = "details";
    private static final String THREAD_NAME_KEY = "name";
    private static final String THREAD_BLOCKED_COUNT_KEY = "blockedCount";
    private static final String THREAD_BLOCKED_TIME_KEY = "blockedTime";
    private static final String THREAD_IN_NATIVE_KEY = "isInNative";
    private static final String THREAD_SUSPENDED_KEY = "suspended";
    private static final String THREAD_ID_KEY = "threadId";
    private static final String THREAD_STATE_KEY = "threadState";
    private static final String THREAD_WAITED_COUNT_KEY = "waitedCount";
    private static final String THREAD_WAITED_TIME_KEY = "waitedTime";
    private static final String THREAD_LOCK_NAME_KEY = "lockName";

    private static final boolean DETAILS = false;

    public ThreadSampleCollector(final MBeanServerConnection mBeanServerConnection) {
        super(requireNonNull(mBeanServerConnection));
    }

    @Override
    public CompletableFuture<Map<String, Object>> collectSample() {
        return CompletableFuture.supplyAsync(() -> {
            final Map<String, Object> threadResultMap = Maps.newLinkedHashMap();
            threadResultMap.put(SAMPLE_KEY, parseThreads(getThreadMXBean(OBJECT_NAME)));
            return threadResultMap;
        });
    }

    private Map<String, Object> parseThreads(final ThreadMXBean proxy) {
        final Map<String, Object> threadDataMap = Maps.newLinkedHashMap();
        threadDataMap.put(THREAD_COUNT_KEY, proxy.getThreadCount());
        threadDataMap.put(THREAD_DAEMON_COUNT_KEY, proxy.getDaemonThreadCount());
        threadDataMap.put(THREAD_PEAK_COUNT_KEY, proxy.getPeakThreadCount());
        threadDataMap.put(THREAD_TOTAL_STARTED_KEY, proxy.getTotalStartedThreadCount());
        threadDataMap.put(THREAD_CURRENT_USER_TIME_KEY, proxy.getCurrentThreadUserTime());
        threadDataMap.put(THREAD_CURRENT_CPU_TIME_KEY, proxy.getCurrentThreadCpuTime());
        if (DETAILS) {
            final List<Map<String, Object>> details = Arrays.stream(proxy.dumpAllThreads(true, true)).map(threadInfo -> {
                final Map<String, Object> detailsMap = Maps.newLinkedHashMap();
                detailsMap.put(THREAD_NAME_KEY, threadInfo.getThreadName());
                detailsMap.put(THREAD_ID_KEY, threadInfo.getThreadId());
                detailsMap.put(THREAD_STATE_KEY, threadInfo.getThreadState());
                detailsMap.put(THREAD_SUSPENDED_KEY, threadInfo.isSuspended());
                detailsMap.put(THREAD_IN_NATIVE_KEY, threadInfo.isInNative());
                detailsMap.put(THREAD_BLOCKED_COUNT_KEY, threadInfo.getBlockedCount());
                detailsMap.put(THREAD_BLOCKED_TIME_KEY, threadInfo.getBlockedTime());
                detailsMap.put(THREAD_WAITED_COUNT_KEY, threadInfo.getWaitedCount());
                detailsMap.put(THREAD_WAITED_TIME_KEY, threadInfo.getWaitedTime());
                detailsMap.put(THREAD_LOCK_NAME_KEY, threadInfo.getLockName());
                return detailsMap;
            }).collect(Collectors.toList());
            threadDataMap.put(THREAD_DETAILS_KEY, details);
        }
        return threadDataMap;
    }

    private ThreadMXBean getThreadMXBean(final String objectName) {
        try {
            return ManagementFactory.newPlatformMXBeanProxy(mBeanServerConnection(), objectName,
                    ThreadMXBean.class);
        } catch (IOException ex) {
            final JmxCollectorException exception = new JmxCollectorException(format("Unable to get %s: %s, cause: %s",
                    objectName, ex.getMessage(), ex.getClass().getSimpleName()));
            LOG.warn(exception.getMessage());
            throw exception;
        }
    }
}
