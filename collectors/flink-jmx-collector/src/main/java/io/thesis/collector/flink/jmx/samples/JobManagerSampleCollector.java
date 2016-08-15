package io.thesis.collector.flink.jmx.samples;

import com.google.common.collect.Maps;
import io.thesis.collector.commons.jmx.AbstractJmxSampleCollector;
import io.thesis.collector.commons.jmx.JmxCollectorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.*;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;

public final class JobManagerSampleCollector extends AbstractJmxSampleCollector {

    public static final String SAMPLE_KEY = "jobmanager";

    private static final Logger LOG = LoggerFactory.getLogger(JobManagerSampleCollector.class);

    public JobManagerSampleCollector(final MBeanServerConnection mBeanServerConnection) {
        super(requireNonNull(mBeanServerConnection));
    }

    @Override
    public CompletableFuture<Map<String, Object>> collectSample() {
        return CompletableFuture.supplyAsync(() -> {
            final Set<String> distinctHosts = getDistinctHosts();
            final List<Map<String, Object>> jobmanagerList = distinctHosts.stream()
                    .map(host -> {
                        final Map<String, Object> dataMap = Maps.newLinkedHashMap();
                        dataMap.put("host", host);
                        dataMap.putAll(parseJobmanagerStats(host));
                        dataMap.putAll(parseJvmStats(host));
                        return dataMap;
                    }).collect(Collectors.toList());

            final Map<String, Object> jobmanagerResultMap = Maps.newLinkedHashMap();
            jobmanagerResultMap.put(SAMPLE_KEY, jobmanagerList);
            return jobmanagerResultMap;
        });
    }

    private Map<String, Object> parseJobmanagerStats(final String host) {
        final String objectNameFormat = format("org.apache.flink.metrics:key0=%s,key1=jobmanager,name=*", host);
        final ObjectName queryName = convertStringToObjectName(objectNameFormat);
        final Set<ObjectName> objectNames = queryObjectNames(queryName);
        if (objectNames.size() != 4) {
            throw new JmxCollectorException(format("The implementation assumes 4 objects, but was: %d", objectNames.size()));
        }
        final Map<String, Object> jmStatsMap = Maps.newLinkedHashMap();
        objectNames.forEach(objectName -> {
            try {
                final String attributeName = objectName.getKeyProperty("name");
                final Object value = mBeanServerConnection().getAttribute(objectName, "Value");
                jmStatsMap.put(attributeName, value);
            } catch (MBeanException | AttributeNotFoundException | InstanceNotFoundException | IOException |
                    ReflectionException ex) {
                LOG.warn("Unable to collect Flink Jobmanager data, cause: {}: {}", ex.getClass().getName(), ex.getMessage());
            }
        });
        return jmStatsMap;
    }

    private Set<String> getDistinctHosts() {
        final String queryHosts = "org.apache.flink.metrics:key0=*,key1=jobmanager,key2=Status," + "key3=JVM,key4=CPU,name=Load";
        final ObjectName queryName = convertStringToObjectName(queryHosts);
        final Set<ObjectName> objectNames = queryObjectNames(queryName);
        return objectNames.stream()
                .map(objectName -> objectName.getKeyProperty("key0"))
                .distinct()
                .collect(Collectors.toSet());
    }

    private Map<String, Object> parseJvmStats(final String host) {
        final Map<String, Object> jvmStatsData = Maps.newLinkedHashMap();
        jvmStatsData.put("cpu", parseCpu(host));
        jvmStatsData.put("classLoader", parseClassLoader(host));
        jvmStatsData.put("threads", parseThreads(host));
        jvmStatsData.put("memory", parseMemory(host));
        jvmStatsData.put("garbageCollector", parseGarbageCollector(host));
        final Map<String, Object> jvmStatsResult = Maps.newLinkedHashMap();
        jvmStatsResult.put("jvm", jvmStatsData);
        return jvmStatsResult;
    }

    private Map<String, Object> parseCpu(final String host) {
        final String objectNameFormat = format("org.apache.flink.metrics:key0=%s,key1=jobmanager,key2=Status,key3=JVM,key4=CPU,name=*", host);
        final ObjectName queryName = convertStringToObjectName(objectNameFormat);
        final Set<ObjectName> objectNames = queryObjectNames(queryName);
        if (objectNames.size() != 2) {
            throw new JmxCollectorException(format("The implementation assumes 2 objects, but was: %d", objectNames.size()));
        }
        final Map<String, Object> cpuResultMap = Maps.newLinkedHashMap();
        objectNames.forEach(objectName -> {
            try {
                final String attributeName = objectName.getKeyProperty("name");
                final Object value = mBeanServerConnection().getAttribute(objectName, "Value");
                cpuResultMap.put(attributeName, value);
            } catch (MBeanException | AttributeNotFoundException | InstanceNotFoundException | IOException |
                    ReflectionException ex) {
                LOG.warn("Unable to collect Flink Jobmanager JVM Cpu data, cause: {}: {}", ex.getClass().getName(), ex.getMessage());
            }
        });
        return cpuResultMap;
    }

    private Map<String, Object> parseClassLoader(final String host) {
        final String objectNameFormat = format("org.apache.flink.metrics:key0=%s,key1=jobmanager,key2=Status,key3=JVM," +
                "key4=ClassLoader,name=*", host);
        final ObjectName queryName = convertStringToObjectName(objectNameFormat);
        final Set<ObjectName> objectNames = queryObjectNames(queryName);
        if (objectNames.size() != 2) {
            throw new JmxCollectorException(format("The implementation assumes 2 objects, but was: %d", objectNames.size()));
        }
        final Map<String, Object> classLoaderResultMap = Maps.newLinkedHashMap();
        objectNames.forEach(objectName -> {
            try {
                final String attributeName = objectName.getKeyProperty("name");
                final Object value = mBeanServerConnection().getAttribute(objectName, "Value");
                classLoaderResultMap.put(attributeName, value);
            } catch (MBeanException | AttributeNotFoundException | InstanceNotFoundException | IOException |
                    ReflectionException ex) {
                LOG.warn("Unable to collect Flink Jobmanager JVM class loader data, cause: {}: {}", ex.getClass().getName(), ex.getMessage());
            }
        });
        return classLoaderResultMap;
    }

    private Map<String, Object> parseThreads(final String host) {
        final String objectNameFormat = format("org.apache.flink.metrics:key0=%s,key1=jobmanager,key2=Status,key3=JVM," +
                "key4=Threads,name=*", host);
        final ObjectName queryName = convertStringToObjectName(objectNameFormat);
        final Set<ObjectName> objectNames = queryObjectNames(queryName);
        if (objectNames.size() != 1) {
            throw new JmxCollectorException(format("The implementation assumes 1 object, but was: %d", objectNames.size()));
        }
        final Map<String, Object> threadsResultMap = Maps.newLinkedHashMap();
        objectNames.forEach(objectName -> {
            try {
                final String attributeName = objectName.getKeyProperty("name");
                final Object value = mBeanServerConnection().getAttribute(objectName, "Value");

                threadsResultMap.put(attributeName, value);
            } catch (MBeanException | AttributeNotFoundException | InstanceNotFoundException | IOException |
                    ReflectionException ex) {
                LOG.warn("Unable to collect Flink Jobmanager JVM thread data, cause: {}: {}", ex.getClass().getName(), ex.getMessage());
            }
        });
        return threadsResultMap;
    }

    private List<Map<String, Object>> parseMemory(final String ip) {
        final Set<String> distinctMemories = getDistinctMemories();
        return distinctMemories.stream().map(memoryName -> {
            final Map<String, Object> memoryMap = Maps.newLinkedHashMap();
            final ObjectName queryName = convertStringToObjectName("org.apache.flink.metrics:key0=" + ip +
                    ",key1=jobmanager,key2=Status,key3=JVM,key4=Memory,key5=" + memoryName + ",name=*");
            final Set<ObjectName> objectNames = queryObjectNames(queryName);
            if (objectNames.size() != 3) {
                throw new JmxCollectorException(format("The implementation assumes 3 objects, but was: %d", objectNames.size()));
            }
            memoryMap.put("name", memoryName);
            objectNames.forEach(objectName -> {
                try {
                    final String attributeName = objectName.getKeyProperty("name");
                    final Object value = mBeanServerConnection().getAttribute(objectName, "Value");
                    memoryMap.put(attributeName, value);
                } catch (MBeanException | AttributeNotFoundException | InstanceNotFoundException | IOException |
                        ReflectionException ex) {
                    LOG.warn("Unable to collect Flink Jobmanager JVM memory data, cause: {}: {}", ex.getClass().getName(), ex.getMessage());
                }
            });
            return memoryMap;
        }).collect(Collectors.toList());
    }

    private List<Map<String, Object>> parseGarbageCollector(final String ip) {
        final Set<String> distinctGarbageCollectors = getDistinctGarbageCollectors();
        return distinctGarbageCollectors.stream()
                .map(gcName -> {
                    final Map<String, Object> gcMap = Maps.newLinkedHashMap();
                    final ObjectName queryName = convertStringToObjectName("org.apache.flink.metrics:key0=" + ip +
                            ",key1=jobmanager,key2=Status,key3=JVM,key4=GarbageCollector,key5=" + gcName + ",name=*");
                    final Set<ObjectName> objectNames = queryObjectNames(queryName);
                    if (objectNames.size() != 2) {
                        throw new JmxCollectorException(format("The implementation assumes 2 objects, but was: %d", objectNames.size()));
                    }
                    gcMap.put("name", gcName);
                    objectNames.forEach(objectName -> {
                        try {
                            final String attributeName = objectName.getKeyProperty("name");
                            final Object value = mBeanServerConnection().getAttribute(objectName, "Value");
                            gcMap.put(attributeName, value);
                        } catch (MBeanException | AttributeNotFoundException | InstanceNotFoundException | IOException |
                                ReflectionException ex) {
                            LOG.warn("Unable to collect Flink Jobmanager JVM thread data, cause: {}: {}", ex.getClass().getName(), ex.getMessage());
                        }
                    });
                    return gcMap;
                }).collect(Collectors.toList());
    }

    private Set<String> getDistinctMemories() {
        final String queryString = "org.apache.flink.metrics:key0=*,key1=jobmanager,key2=Status,key3=JVM,key4=Memory," +
                "key5=*,name=*";
        final ObjectName queryName = convertStringToObjectName(queryString);
        final Set<ObjectName> objectNames = queryObjectNames(queryName);
        return objectNames.stream()
                .map(objectName -> objectName.getKeyProperty("key5"))
                .distinct()
                .collect(Collectors.toSet());
    }

    private Set<String> getDistinctGarbageCollectors() {
        final String queryString = "org.apache.flink.metrics:key0=*,key1=jobmanager,key2=Status,key3=JVM,key4=GarbageCollector," +
                "key5=*,name=Count";
        final ObjectName queryName = convertStringToObjectName(queryString);
        final Set<ObjectName> objectNames = queryObjectNames(queryName);
        return objectNames.stream()
                .map(objectName -> objectName.getKeyProperty("key5"))
                .distinct()
                .collect(Collectors.toSet());
    }
}
