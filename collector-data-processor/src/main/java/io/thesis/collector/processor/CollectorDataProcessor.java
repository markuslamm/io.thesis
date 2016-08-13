package io.thesis.collector.processor;

import io.thesis.commons.json.JsonUtils;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer09;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer09;
import org.apache.flink.streaming.util.serialization.SimpleStringSchema;

import java.util.*;

/**
 * This is just av very simple Flink job, which receives all messages from collector-clients,
 * filters messages of collector type 'flink_jmx', extracts containing JVM data and writes the
 * flattened map into Elasticsearch.
 */
public class CollectorDataProcessor {

    private static final String KAFKA_HOST = "192.168.2.105:9092";
    private static final String INBOUND_TOPIC = "collector-outbound-topic";
    private static final String OUTBOUND_TOPIC = "flink-outbound-topic";

    public static void main(String[] args) throws Exception {

        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        final Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", KAFKA_HOST);

        final DataStream<CollectorResult> mappedStream =
                env.addSource(new FlinkKafkaConsumer09<>(INBOUND_TOPIC, new SimpleStringSchema(), properties))
                        .flatMap((rawJsonInput, out) -> {
                            final CollectorResult collectorResult = JsonUtils.readObject(CollectorResult.class, rawJsonInput);
                            out.collect(collectorResult);
                        });

        final DataStream<CollectorResult> collectorFlinkJmxStream = mappedStream.filter(collectorResult ->
                collectorResult.getCollectorType().equals("flink_jmx"));

        final DataStream<String> resultStream = collectorFlinkJmxStream.map(flinkJmxResult -> {
            final Map<String, Object> dataMap = new LinkedHashMap<>();
            dataMap.put("client-timestamp", extractOrUnknown(flinkJmxResult.getClientTimestamp()));
            dataMap.put("client-host", extractOrUnknown(flinkJmxResult.getClientHost()));
            dataMap.put("client-port", extractOrUnknown(flinkJmxResult.getClientPort()));
            dataMap.put("client-instance-id", extractOrUnknown(flinkJmxResult.getInstanceId()));
            final Map<String, Object> jvmMap = Optional.ofNullable(flinkJmxResult.getData())
                    .map(CollectorDataProcessor::extractJvmData)
                    .orElse(new LinkedHashMap<>());
            dataMap.putAll(jvmMap);
            return JsonUtils.toJson(dataMap);
        });
        resultStream.addSink(new FlinkKafkaProducer09<>(KAFKA_HOST, OUTBOUND_TOPIC, new SimpleStringSchema()));
        env.execute();
    }

    private static String extractOrUnknown(final Object value) {
        return Optional.ofNullable(value).map(Object::toString).orElse("unknown");
    }

    private static Map<String, Object> extractJvmData(final Map<String, Object> data) {
        final Object jobmanager = Optional.ofNullable(data.get("jobmanager"))
                .orElseThrow(() -> new CollectorDataProcessorException("Invalid data, 'jobmanager' element required"));
        final Map<String, Object> jvmResultMap = new LinkedHashMap<>();
        if (jobmanager instanceof List) {
            final List<Map<String, Object>> jm = (List<Map<String, Object>>) jobmanager;
            //TODO assuming only one jobmanager for prototype, null handling, shitty at all
            final Map<String, Object> jmMap = jm.get(0);
            final Object jvm = jmMap.get("jvm");
            if (jvm instanceof Map) {
                final Map<String, Object> jvmMap = (Map<String, Object>) jvm;
                final Object cpu = jvmMap.get("cpu");
                if (cpu instanceof Map) {
                    final Map<String, Object> cpuMap = (Map<String, Object>) cpu;
                    jvmResultMap.put("cpu-load", extractOrUnknown(cpuMap.get("Load")));
                    jvmResultMap.put("cpu-time", extractOrUnknown(cpuMap.get("Time")));
                }
            }
        }
        return jvmResultMap;
    }
}

