package io.thesis.collector.kafka.jmx.broker.samples;

import io.thesis.collector.kafka.jmx.broker.AbstractJmxIT;
import io.thesis.commons.json.JsonUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class KafkaControllerSampleCollectorIT extends AbstractJmxIT {

    private static final String JMX_URL = "service:jmx:rmi:///jndi/rmi://localhost:9997/jmxrmi";

    private KafkaControllerSampleCollector sampleCollector;

    @Before
    public void setUp() throws IOException {
        sampleCollector = new KafkaControllerSampleCollector(mBeanServerConnection());
    }

    @Test
    public void testCollectSample() {
        final Map<String, Object> result = sampleCollector.collectSample().join();
        System.err.println(JsonUtils.toJson(result));
        assertThat(result).isNotNull();
        assertThat(result).isNotEmpty();
        assertThat(result.get(KafkaControllerSampleCollector.SAMPLE_KEY)).isNotNull();
        assertThat(result.get(KafkaControllerSampleCollector.SAMPLE_KEY) instanceof Map).isTrue();
    }

    @Override
    protected String getJmxUrl() {
        return JMX_URL;
    }
}
