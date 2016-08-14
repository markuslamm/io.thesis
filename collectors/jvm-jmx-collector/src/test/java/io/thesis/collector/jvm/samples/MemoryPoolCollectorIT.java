package io.thesis.collector.jvm.samples;

import io.thesis.collector.jvm.AbstractJmxIT;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class MemoryPoolCollectorIT extends AbstractJmxIT {

    private MemoryPoolSampleCollector memoryPoolSampleCollector;

    @Before
    public void setUp() throws IOException {
        memoryPoolSampleCollector = new MemoryPoolSampleCollector(mBeanServerConnection());
    }

    @Test
    public void testCollectSample() {
        final Map<String, Object> result = memoryPoolSampleCollector.collectSample().join();
        assertThat(result).isNotNull();
        assertThat(result).isNotEmpty();
        assertThat(result.get(MemoryPoolSampleCollector.SAMPLE_KEY)).isNotNull();
        assertThat(result.get(MemoryPoolSampleCollector.SAMPLE_KEY) instanceof List).isTrue();
    }
}
