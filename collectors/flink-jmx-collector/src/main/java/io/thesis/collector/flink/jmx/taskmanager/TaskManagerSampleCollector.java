package io.thesis.collector.flink.jmx.taskmanager;

import io.thesis.collector.commons.jmx.AbstractJmxSampleCollector;

import javax.management.MBeanServerConnection;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static java.util.Objects.requireNonNull;

public final class TaskManagerSampleCollector extends AbstractJmxSampleCollector {

    static final String SAMPLE_KEY = "taskmanager";

    public TaskManagerSampleCollector(final MBeanServerConnection mBeanServerConnection) {
        super(requireNonNull(mBeanServerConnection));
    }

    @Override
    public CompletableFuture<Map<String, Object>> collectSample() {
        throw new UnsupportedOperationException("TaskManagerSampleCollector not implemented yet");
    }
}
