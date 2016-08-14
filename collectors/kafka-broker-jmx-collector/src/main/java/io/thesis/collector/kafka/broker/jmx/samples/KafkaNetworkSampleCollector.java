package io.thesis.collector.kafka.broker.jmx.samples;

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

/**
 * Collects data from 'kafka.network:type=...' MBeans.
 */
public class KafkaNetworkSampleCollector extends AbstractJmxSampleCollector {

    public static final String SAMPLE_KEY = "kafka-network";

    public KafkaNetworkSampleCollector(final MBeanServerConnection mBeanServerConnection) {
        super(requireNonNull(mBeanServerConnection));
    }

    @Override
    public CompletableFuture<Map<String, Object>> collectSample() {
        return CompletableFuture.supplyAsync(() -> {
            final Map<String, Object> networkDataMap = Maps.newLinkedHashMap();
            networkDataMap.put("idlePercent", parseIdlePercent());
            final Map<String, Object> networkResultMap = Maps.newLinkedHashMap();
            networkResultMap.put(SAMPLE_KEY, networkDataMap);
            return networkResultMap;
        });
    }

    private List<Map<String, Object>> parseIdlePercent() {
        final String queryString = "kafka.network:type=Processor,name=IdlePercent,networkProcessor=*";
        final ObjectName objectName = convertStringToObjectName(queryString);
        final Set<ObjectName> objectNames = queryObjectNames(objectName);
        return objectNames.stream().map(object -> {
            final Map<String, Object> dataMap = parseAllAttributes(object);
            dataMap.put("networkProcessor", object.getKeyProperty("networkProcessor"));
            return dataMap;
        }).collect(Collectors.toList());
    }
}
