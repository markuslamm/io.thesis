package io.thesis.collector.kafka.jmx.broker;

import io.thesis.collector.commons.CollectorResult;
import io.thesis.collector.commons.CollectorType;
import io.thesis.commons.json.JsonUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class KafkaBrokerJmxCollectorIT extends AbstractJmxIT {

    private static final String JMX_URL = "service:jmx:rmi:///jndi/rmi://localhost:9997/jmxrmi";

    private KafkaBrokerJmxCollector kafkaBrokerJmxCollector;

    @Before
    public void setUp() throws IOException {
        kafkaBrokerJmxCollector = new KafkaBrokerJmxCollector(mBeanServerConnection());
    }

    @Test
    public void testCollect() {
        final CollectorResult result = kafkaBrokerJmxCollector.collect().join();
        System.err.println(JsonUtils.toJson(result));
        assertThat(result).isNotNull();
        assertThat(result.getClientTimestamp()).isNull();
        assertThat(result.getClientHost()).isNull();
        assertThat(result.getInstanceId()).isNull();
        assertThat(result.getCollectorType()).isNotNull();
        assertThat(result.getCollectorType()).isEqualTo(CollectorType.KAFKA_BROKER_JMX.name().toLowerCase());
        assertThat(result.getData()).isNotNull();
        assertThat(result.getData().isEmpty()).isFalse();
        assertThat(result.getData().get("kafka-controller")).isNotNull();
        assertThat(result.getData().get("kafka-coordinator")).isNotNull();
        assertThat(result.getData().get("kafka-network")).isNotNull();
        assertThat(result.getData().get("kafka-server")).isNotNull();
    }

    @Override
    protected String getJmxUrl() {
        return JMX_URL;
    }
}
