package io.thesis.collector.kafka.broker.jmx.samples;

import io.thesis.collector.kafka.broker.jmx.AbstractJmxIT;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class KafkaServerSampleCollectorIT extends AbstractJmxIT {

    private static final String JMX_URL = "service:jmx:rmi:///jndi/rmi://localhost:9997/jmxrmi";

    private KafkaServerSampleCollector sampleCollector;

    @Before
    public void setUp() throws IOException {
        sampleCollector = new KafkaServerSampleCollector(mBeanServerConnection());
    }

    @Test
    public void testCollectSample() {
        final Map<String, Object> result = sampleCollector.collectSample().join();
        assertThat(result).isNotNull();
        assertThat(result).isNotEmpty();
        assertThat(result.get(KafkaServerSampleCollector.SAMPLE_KEY)).isNotNull();
        assertThat(result.get(KafkaServerSampleCollector.SAMPLE_KEY) instanceof Map).isTrue();
    }

    @Override
    protected String getJmxUrl() {
        return JMX_URL;
    }
}
