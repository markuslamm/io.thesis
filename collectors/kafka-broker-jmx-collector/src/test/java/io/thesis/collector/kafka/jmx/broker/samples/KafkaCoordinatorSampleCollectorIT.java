package io.thesis.collector.kafka.jmx.broker.samples;

import io.thesis.collector.kafka.jmx.broker.AbstractJmxIT;
import io.thesis.commons.json.JsonUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class KafkaCoordinatorSampleCollectorIT extends AbstractJmxIT {

    private static final String JMX_URL = "service:jmx:rmi:///jndi/rmi://localhost:9997/jmxrmi";

    private KafkaCoordinatorSampleCollector sampleCollector;

    @Before
    public void setUp() throws IOException {
        sampleCollector = new KafkaCoordinatorSampleCollector(mBeanServerConnection());
    }

    @Test
    public void testCollectSample() {
        final Map<String, Object> result = sampleCollector.collectSample().join();
        System.err.println(JsonUtils.toJson(result));
        assertThat(result).isNotNull();
        assertThat(result).isNotEmpty();
        assertThat(result.get(KafkaCoordinatorSampleCollector.SAMPLE_KEY)).isNotNull();
        assertThat(result.get(KafkaCoordinatorSampleCollector.SAMPLE_KEY) instanceof Map).isTrue();
    }

    @Override
    protected String getJmxUrl() {
        return JMX_URL;
    }
}
