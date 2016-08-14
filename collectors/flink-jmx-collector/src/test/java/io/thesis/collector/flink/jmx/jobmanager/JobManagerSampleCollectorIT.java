package io.thesis.collector.flink.jmx.jobmanager;

import io.thesis.collector.flink.jmx.AbstractJmxIT;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class JobManagerSampleCollectorIT extends AbstractJmxIT {

    private static final String JMX_URL = "service:jmx:rmi:///jndi/rmi://localhost:9999/jmxrmi";

    private JobManagerSampleCollector jobManagerSampleCollector;

    @Before
    public void setUp() throws IOException {
        jobManagerSampleCollector = new JobManagerSampleCollector(mBeanServerConnection());
    }

    @Test
    public void testCollectSample() {
        final Map<String, Object> result = jobManagerSampleCollector.collectSample().join();
        assertThat(result).isNotNull();
        assertThat(result).isNotEmpty();
        assertThat(result.get(JobManagerSampleCollector.SAMPLE_KEY)).isNotNull();
        assertThat(result.get(JobManagerSampleCollector.SAMPLE_KEY) instanceof List).isTrue();
    }

    @Override
    protected String getJmxUrl() {
        return JMX_URL;
    }
}
