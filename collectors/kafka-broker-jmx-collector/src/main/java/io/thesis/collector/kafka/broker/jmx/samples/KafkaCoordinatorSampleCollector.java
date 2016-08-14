package io.thesis.collector.kafka.broker.jmx.samples;

import com.google.common.collect.Maps;
import io.thesis.collector.commons.jmx.AbstractJmxSampleCollector;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static java.util.Objects.requireNonNull;

/**
 * Collects data from 'kafka.coordinator:type=...' MBeans.
 */
public class KafkaCoordinatorSampleCollector extends AbstractJmxSampleCollector {

    public static final String SAMPLE_KEY = "kafka-coordinator";

    public KafkaCoordinatorSampleCollector(final MBeanServerConnection mBeanServerConnection) {
        super(requireNonNull(mBeanServerConnection));
    }

    @Override
    public CompletableFuture<Map<String, Object>> collectSample() {
        return CompletableFuture.supplyAsync(() -> {
            final Map<String, Object> coordinatorDataMap = Maps.newLinkedHashMap();
            coordinatorDataMap.put("numGroups", parseNumGroups());
            coordinatorDataMap.put("numOffsets", parseNumOffsets());
            final Map<String, Object> coordinatorResultMap = Maps.newLinkedHashMap();
            coordinatorResultMap.put(SAMPLE_KEY, coordinatorDataMap);
            return coordinatorResultMap;
        });
    }

    private Map<String, Object> parseNumGroups() {
        final String queryString = "kafka.coordinator:type=GroupMetadataManager,name=NumGroups";
        final ObjectName objectName = getSingleQueryObject(queryString);
        return parseAllAttributes(objectName);
    }

    private Map<String, Object> parseNumOffsets() {
        final String queryString = "kafka.coordinator:type=GroupMetadataManager,name=NumOffsets";
        final ObjectName objectName = getSingleQueryObject(queryString);
        return parseAllAttributes(objectName);
    }
}
