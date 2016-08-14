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
 * Collects data from 'kafka.server:type=...' MBeans.
 */
public class KafkaServerSampleCollector extends AbstractJmxSampleCollector {

    public static final String SAMPLE_KEY = "kafka-server";

    public KafkaServerSampleCollector(final MBeanServerConnection mBeanServerConnection) {
        super(requireNonNull(mBeanServerConnection));
    }

    @Override
    public CompletableFuture<Map<String, Object>> collectSample() {
        return CompletableFuture.supplyAsync(() -> {
            final Map<String, Object> serverDataMap = Maps.newLinkedHashMap();
            serverDataMap.put("brokerTopicMetrics", parseBrokerTopicMetrics());
            serverDataMap.put("requestHandlerPool", parseRequestHandlerPool());
            serverDataMap.put("replicaManager", parseReplicaManager());
            serverDataMap.put("controllerChannelMetrics", parseControllerChannelMetrics());
            serverDataMap.put("socketServerMetrics", parseSocketServerMetrics());
            final Map<String, Object> serverResultMap = Maps.newLinkedHashMap();
            serverResultMap.put(SAMPLE_KEY, serverDataMap);
            return serverResultMap;
        });
    }

    private List<Map<String, Object>> parseSocketServerMetrics() {
        final String queryString = "kafka.server:type=socket-server-metrics,networkProcessor=*";
        final ObjectName objectName = convertStringToObjectName(queryString);
        final Set<ObjectName> objectNames = queryObjectNames(objectName);
        return objectNames.stream().map(object -> {
            final Map<String, Object> dataMap = parseAllAttributes(object);
            dataMap.put("networkProcessor", object.getKeyProperty("networkProcessor"));
            return dataMap;
        }).collect(Collectors.toList());
    }

    private List<Map<String, Object>> parseControllerChannelMetrics() {
        final String queryString = "kafka.server:type=controller-channel-metrics,broker-id=*";
        final ObjectName objectName = convertStringToObjectName(queryString);
        final Set<ObjectName> objectNames = queryObjectNames(objectName);
        return objectNames.stream().map(object -> {
            final Map<String, Object> dataMap = parseAllAttributes(object);
            dataMap.put("brokerId", object.getKeyProperty("broker-id"));
            return dataMap;
        }).collect(Collectors.toList());

    }


    private Map<String, Object> parseReplicaManager() {
        final Map<String, Object> dataMap = Maps.newLinkedHashMap();
        dataMap.put("isrExpandsPerSec", parseIsrExpandsPerSec());
        dataMap.put("isrShrinksPerSec", parseIsrShrinksPerSec());
        dataMap.put("leaderCount", parseLeaderCount());
        dataMap.put("partitionCount", parsePartitionCount());
        dataMap.put("underReplicatedPartitions", parseUnderReplicatedPartitions());
        return dataMap;
    }

    private Map<String, Object> parseIsrExpandsPerSec() {
        final String queryString = "kafka.server:type=ReplicaManager,name=IsrExpandsPerSec";
        final ObjectName objectName = getSingleQueryObject(queryString);
        return parseAllAttributes(objectName);
    }

    private Map<String, Object> parseIsrShrinksPerSec() {
        final String queryString = "kafka.server:type=ReplicaManager,name=IsrShrinksPerSec";
        final ObjectName objectName = getSingleQueryObject(queryString);
        return parseAllAttributes(objectName);
    }

    private Map<String, Object> parseLeaderCount() {
        final String queryString = "kafka.server:type=ReplicaManager,name=LeaderCount";
        final ObjectName objectName = getSingleQueryObject(queryString);
        return parseAllAttributes(objectName);
    }

    private Map<String, Object> parsePartitionCount() {
        final String queryString = "kafka.server:type=ReplicaManager,name=PartitionCount";
        final ObjectName objectName = getSingleQueryObject(queryString);
        return parseAllAttributes(objectName);
    }

    private Map<String, Object> parseUnderReplicatedPartitions() {
        final String queryString = "kafka.server:type=ReplicaManager,name=UnderReplicatedPartitions";
        final ObjectName objectName = getSingleQueryObject(queryString);
        return parseAllAttributes(objectName);
    }

    private Map<String, Object> parseRequestHandlerPool() {
        final Map<String, Object> dataMap = Maps.newLinkedHashMap();
        dataMap.put("requestHandlerAvgIdlePercent", parseRequestHandlerAvgIdlePercent());
        return dataMap;
    }

    private Map<String, Object> parseRequestHandlerAvgIdlePercent() {
        final String queryString = "kafka.server:type=KafkaRequestHandlerPool,name=RequestHandlerAvgIdlePercent";
        final ObjectName objectName = getSingleQueryObject(queryString);
        return parseAllAttributes(objectName);
    }


    private Map<String, Object> parseBrokerTopicMetrics() {
        final Map<String, Object> dataMap = Maps.newLinkedHashMap();
        dataMap.put("bytesInPerSec", parseBytesInPerSec());
        dataMap.put("bytesOutPerSec", parseBytesOutPerSec());
        dataMap.put("bytesRejectedPerSec", parseBytesRejectedPerSec());
        dataMap.put("failedFetchRequestsPerSec", parseFailedFetchRequestsPerSec());
        dataMap.put("failedProduceRequestsPerSec", parseFailedProduceRequestsPerSec());
        dataMap.put("messagesInPerSec", parseMessagesInPerSec());
        dataMap.put("totalFetchRequestsPerSec", parseTotalFetchRequestsPerSec());
        dataMap.put("totalProduceRequestsPerSec", parseTotalProduceRequestsPerSec());
        return dataMap;
    }

    private Map<String, Object> parseTotalProduceRequestsPerSec() {
        final String queryString = "kafka.server:type=BrokerTopicMetrics,name=TotalProduceRequestsPerSec";
        final ObjectName objectName = getSingleQueryObject(queryString);
        final Map<String, Object> dataMap = parseAllAttributes(objectName);
        dataMap.put("topics", parseTotalProduceRequestsPerSecForTopics());
        return dataMap;
    }

    private List<Map<String, Object>> parseTotalProduceRequestsPerSecForTopics() {
        final String queryString = "kafka.server:type=BrokerTopicMetrics,name=TotalProduceRequestsPerSec,topic=*";
        final ObjectName objectName = convertStringToObjectName(queryString);
        final Set<ObjectName> objectNames = queryObjectNames(objectName);

        return objectNames.stream().map(object -> {
            final Map<String, Object> dataMap = parseAllAttributes(object);
            dataMap.put("name", object.getKeyProperty("topic"));
            return dataMap;
        }).collect(Collectors.toList());
    }

    private Map<String, Object> parseTotalFetchRequestsPerSec() {
        final String queryString = "kafka.server:type=BrokerTopicMetrics,name=TotalFetchRequestsPerSec";
        final ObjectName objectName = getSingleQueryObject(queryString);
        final Map<String, Object> dataMap = parseAllAttributes(objectName);
        dataMap.put("topics", parseTotalFetchRequestsPerSecForTopics());
        return dataMap;
    }

    private List<Map<String, Object>> parseTotalFetchRequestsPerSecForTopics() {
        final String queryString = "kafka.server:type=BrokerTopicMetrics,name=TotalFetchRequestsPerSec,topic=*";
        final ObjectName objectName = convertStringToObjectName(queryString);
        final Set<ObjectName> objectNames = queryObjectNames(objectName);

        return objectNames.stream().map(object -> {
            final Map<String, Object> dataMap = parseAllAttributes(object);
            dataMap.put("name", object.getKeyProperty("topic"));
            return dataMap;
        }).collect(Collectors.toList());
    }

    private Map<String, Object> parseBytesInPerSec() {
        final String queryString = "kafka.server:type=BrokerTopicMetrics,name=BytesInPerSec";
        final ObjectName objectName = getSingleQueryObject(queryString);
        final Map<String, Object> dataMap = parseAllAttributes(objectName);
        dataMap.put("topics", parseBytesInPerSecForTopics());
        return dataMap;
    }

    private List<Map<String, Object>> parseBytesInPerSecForTopics() {
        final String queryString = "kafka.server:type=BrokerTopicMetrics,name=BytesInPerSec,topic=*";
        final ObjectName objectName = convertStringToObjectName(queryString);
        final Set<ObjectName> objectNames = queryObjectNames(objectName);

        return objectNames.stream().map(object -> {
            final Map<String, Object> dataMap = parseAllAttributes(object);
            dataMap.put("name", object.getKeyProperty("topic"));
            return dataMap;
        }).collect(Collectors.toList());
    }

    private Map<String, Object> parseBytesOutPerSec() {
        final String queryString = "kafka.server:type=BrokerTopicMetrics,name=BytesOutPerSec";
        final ObjectName objectName = getSingleQueryObject(queryString);
        final Map<String, Object> dataMap = parseAllAttributes(objectName);
        dataMap.put("topics", parseBytesOutPerSecForTopics());
        return dataMap;
    }

    private List<Map<String, Object>> parseBytesOutPerSecForTopics() {
        final String queryString = "kafka.server:type=BrokerTopicMetrics,name=BytesOutPerSec,topic=*";
        final ObjectName objectName = convertStringToObjectName(queryString);
        final Set<ObjectName> objectNames = queryObjectNames(objectName);

        return objectNames.stream().map(object -> {
            final Map<String, Object> dataMap = parseAllAttributes(object);
            dataMap.put("name", object.getKeyProperty("topic"));
            return dataMap;
        }).collect(Collectors.toList());
    }

    private Map<String, Object> parseBytesRejectedPerSec() {
        final String queryString = "kafka.server:type=BrokerTopicMetrics,name=BytesRejectedPerSec";
        final ObjectName objectName = getSingleQueryObject(queryString);
        final Map<String, Object> dataMap = parseAllAttributes(objectName);
        dataMap.put("topics", parseBytesRejectedPerSecForTopics());
        return dataMap;
    }

    private List<Map<String, Object>> parseBytesRejectedPerSecForTopics() {
        final String queryString = "kafka.server:type=BrokerTopicMetrics,name=BytesRejectedPerSec,topic=*";
        final ObjectName objectName = convertStringToObjectName(queryString);
        final Set<ObjectName> objectNames = queryObjectNames(objectName);

        return objectNames.stream().map(object -> {
            final Map<String, Object> dataMap = parseAllAttributes(object);
            dataMap.put("name", object.getKeyProperty("topic"));
            return dataMap;
        }).collect(Collectors.toList());
    }

    private Map<String, Object> parseFailedFetchRequestsPerSec() {
        final String queryString = "kafka.server:type=BrokerTopicMetrics,name=FailedFetchRequestsPerSec";
        final ObjectName objectName = getSingleQueryObject(queryString);
        final Map<String, Object> dataMap = parseAllAttributes(objectName);
        dataMap.put("topics", parseBytesRejectedPerSecForTopics());
        return dataMap;
    }

    private List<Map<String, Object>> parseFailedFetchRequestsPerSecForTopics() {
        final String queryString = "kafka.server:type=BrokerTopicMetrics,name=FailedFetchRequestsPerSec,topic=*";
        final ObjectName objectName = convertStringToObjectName(queryString);
        final Set<ObjectName> objectNames = queryObjectNames(objectName);

        return objectNames.stream().map(object -> {
            final Map<String, Object> dataMap = parseAllAttributes(object);
            dataMap.put("name", object.getKeyProperty("topic"));
            return dataMap;
        }).collect(Collectors.toList());
    }

    private Map<String, Object> parseFailedProduceRequestsPerSec() {
        final String queryString = "kafka.server:type=BrokerTopicMetrics,name=FailedProduceRequestsPerSec";
        final ObjectName objectName = getSingleQueryObject(queryString);
        final Map<String, Object> dataMap = parseAllAttributes(objectName);
        dataMap.put("topics", parseFailedProduceRequestsPerSecForTopics());
        return dataMap;
    }

    private List<Map<String, Object>> parseFailedProduceRequestsPerSecForTopics() {
        final String queryString = "kafka.server:type=BrokerTopicMetrics,name=FailedProduceRequestsPerSec,topic=*";
        final ObjectName objectName = convertStringToObjectName(queryString);
        final Set<ObjectName> objectNames = queryObjectNames(objectName);

        return objectNames.stream().map(object -> {
            final Map<String, Object> dataMap = parseAllAttributes(object);
            dataMap.put("name", object.getKeyProperty("topic"));
            return dataMap;
        }).collect(Collectors.toList());
    }

    private Map<String, Object> parseMessagesInPerSec() {
        final String queryString = "kafka.server:type=BrokerTopicMetrics,name=MessagesInPerSec";
        final ObjectName objectName = getSingleQueryObject(queryString);
        final Map<String, Object> dataMap = parseAllAttributes(objectName);
        dataMap.put("topics", parseMessagesInPerSecForTopics());
        return dataMap;
    }

    private List<Map<String, Object>> parseMessagesInPerSecForTopics() {
        final String queryString = "kafka.server:type=BrokerTopicMetrics,name=MessagesInPerSec,topic=*";
        final ObjectName objectName = convertStringToObjectName(queryString);
        final Set<ObjectName> objectNames = queryObjectNames(objectName);

        return objectNames.stream().map(object -> {
            final Map<String, Object> dataMap = parseAllAttributes(object);
            dataMap.put("name", object.getKeyProperty("topic"));
            return dataMap;
        }).collect(Collectors.toList());
    }


}
