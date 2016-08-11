package io.thesis.collector.jvm.samples;

import io.thesis.collector.jvm.AbstractJmxIT;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class MemoryCollectorIT extends AbstractJmxIT {

    private MemorySampleCollector memorySampleCollector;

    @Before
    public void setUp() throws IOException {
        memorySampleCollector = new MemorySampleCollector(mBeanServerConnection());
    }

    @Test
    public void testCollectSample() {
        final Map<String, Object> result = memorySampleCollector.collectSample().join();
        assertThat(result).isNotNull();
        assertThat(result).isNotEmpty();
        assertThat(result.get(MemorySampleCollector.SAMPLE_KEY)).isNotNull();
        assertThat(result.get(MemorySampleCollector.SAMPLE_KEY) instanceof Map).isTrue();
    }
}
