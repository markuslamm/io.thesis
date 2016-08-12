package io.thesis.collector.jvm.samples;

import io.thesis.collector.jvm.AbstractJmxIT;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class RuntimeCollectorIT extends AbstractJmxIT {

    private RuntimeSampleCollector runtimeSampleCollector;

    @Before
    public void setUp() throws IOException {
        runtimeSampleCollector = new RuntimeSampleCollector(mBeanServerConnection());
    }

    @Test
    public void testCollectSample() {
        final Map<String, Object> result = runtimeSampleCollector.collectSample().join();
        assertThat(result).isNotNull();
        assertThat(result).isNotEmpty();
        assertThat(result.get(RuntimeSampleCollector.SAMPLE_KEY)).isNotNull();
        assertThat(result.get(RuntimeSampleCollector.SAMPLE_KEY) instanceof Map).isTrue();
    }
}
