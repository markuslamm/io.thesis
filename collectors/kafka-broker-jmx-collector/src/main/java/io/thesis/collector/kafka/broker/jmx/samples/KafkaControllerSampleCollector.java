package io.thesis.collector.kafka.broker.jmx.samples;

import com.google.common.collect.Maps;
import io.thesis.collector.commons.jmx.AbstractJmxSampleCollector;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static java.util.Objects.requireNonNull;

/**
 * Collects data from 'kafka.controller:type=...' MBeans.
 */
public class KafkaControllerSampleCollector extends AbstractJmxSampleCollector {

    public static final String SAMPLE_KEY = "kafka-controller";

    public KafkaControllerSampleCollector(final MBeanServerConnection mBeanServerConnection) {
        super(requireNonNull(mBeanServerConnection));
    }

    @Override
    public CompletableFuture<Map<String, Object>> collectSample() {
        return CompletableFuture.supplyAsync(() -> {
            final Map<String, Object> controllerDataMap = Maps.newLinkedHashMap();
            controllerDataMap.put("leaderElectionRateAndTimeMs", parseLeaderElectionRateAndTimeMs());
            controllerDataMap.put("uncleanLeaderElectionsPerSec", parseUncleanLeaderElectionsPerSec());
            controllerDataMap.put("activeControllerCount", parseActiveControllerCount());
            controllerDataMap.put("offlinePartitionsCount", parseOfflinePartitionsCount());
            controllerDataMap.put("preferredReplicaImbalanceCount", parsePreferredReplicaImbalanceCount());
            final Map<String, Object> controllerResultMap = Maps.newLinkedHashMap();
            controllerResultMap.put(SAMPLE_KEY, controllerDataMap);
            return controllerResultMap;
        });
    }

    private Map<String, Object> parseLeaderElectionRateAndTimeMs() {
        final String queryString = "kafka.controller:type=ControllerStats,name=LeaderElectionRateAndTimeMs";
        final ObjectName objectName = getSingleQueryObject(queryString);
        return parseAllAttributes(objectName);
    }

    private Map<String, Object> parseUncleanLeaderElectionsPerSec() {
        final String queryString = "kafka.controller:type=ControllerStats,name=UncleanLeaderElectionsPerSec";
        final ObjectName objectName = getSingleQueryObject(queryString);
        return parseAllAttributes(objectName);
    }

    private Map<String, Object> parseActiveControllerCount() {
        final String queryString = "kafka.controller:type=KafkaController,name=ActiveControllerCount";
        final ObjectName objectName = getSingleQueryObject(queryString);
        return parseAllAttributes(objectName);
    }

    private Map<String, Object> parseOfflinePartitionsCount() {
        final String queryString = "kafka.controller:type=KafkaController,name=OfflinePartitionsCount";
        final ObjectName objectName = getSingleQueryObject(queryString);
        return parseAllAttributes(objectName);
    }

    private Map<String, Object> parsePreferredReplicaImbalanceCount() {
        final String queryString = "kafka.controller:type=KafkaController,name=PreferredReplicaImbalanceCount";
        final ObjectName objectName = getSingleQueryObject(queryString);
        return parseAllAttributes(objectName);
    }
}
