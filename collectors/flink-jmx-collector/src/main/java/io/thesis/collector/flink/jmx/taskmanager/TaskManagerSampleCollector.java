package io.thesis.collector.flink.jmx.taskmanager;

import com.google.common.collect.Maps;
import io.thesis.collector.commons.jmx.AbstractJmxSampleCollector;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

public final class TaskManagerSampleCollector extends AbstractJmxSampleCollector {

    static final String SAMPLE_KEY = "taskmanager";

    public TaskManagerSampleCollector(final MBeanServerConnection mBeanServerConnection) {
        super(requireNonNull(mBeanServerConnection));
    }

    @Override
    public CompletableFuture<Map<String, Object>> collectSample() {
        return CompletableFuture.supplyAsync(() -> {
            final Set<String> distinctHosts = getDistinctHosts();
            final List<Map<String, Object>> hostList = distinctHosts.stream()
                    .map(host -> {
                        final Map<String, Object> dataMap = Maps.newLinkedHashMap();
                        dataMap.put("host", host);
//                        dataMap.putAll(parseTaskManagerStats(host));
//                        dataMap.putAll(parseJvmStats(host));
                        return dataMap;
                    }).collect(Collectors.toList());

            final Map<String, Object> taskmanagerResultMap = Maps.newLinkedHashMap();
            taskmanagerResultMap.put(SAMPLE_KEY, hostList);
            return taskmanagerResultMap;
        });
    }


    private Set<String> getDistinctHosts() {
        final String queryHosts = "org.apache.flink.metrics:key0=*,key1=taskmanager,key2=*,key3=Status,key4=JVM,key5=CPU,name=Load";
        final ObjectName queryName = convertStringToObjectName(queryHosts);
        final Set<ObjectName> objectNames = queryObjectNames(queryName);
        return objectNames.stream()
                .map(objectName -> objectName.getKeyProperty("key0"))
                .distinct()
                .collect(Collectors.toSet());
    }

    //<node>-taskmanager-<taskmanagerId>-status-jvm-...
    //<node>-taskmanager-<taskmanagerId>-<job_name>.<task_name>

    private Set<String> getDistinctTaskmanagers(final String host) {
        final String queryHosts = String.format(
                "org.apache.flink.metrics:key0=%s,key1=taskmanager,key2=*,key3=Status,key4=JVM,key5=CPU,name=Load", host);
        final ObjectName queryName = convertStringToObjectName(queryHosts);
        final Set<ObjectName> objectNames = queryObjectNames(queryName);
        return objectNames.stream()
                .map(objectName -> objectName.getKeyProperty("key2"))
                .distinct()
                .collect(Collectors.toSet());
    }
}
