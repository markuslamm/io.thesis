package io.thesis.collector.jvm.samples;

import io.thesis.collector.jvm.AbstractJmxIT;
import io.thesis.commons.json.JsonUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class BufferPoolCollectorIT extends AbstractJmxIT {

    private BufferPoolSampleCollector bufferPoolSampleCollector;

    @Before
    public void setUp() throws IOException {
        bufferPoolSampleCollector = new BufferPoolSampleCollector(mBeanServerConnection());
    }

    @Test
    public void testCollectSample() {
        final Map<String, Object> result = bufferPoolSampleCollector.collectSample().join();
        System.err.println(JsonUtils.toJson(result));
        assertThat(result).isNotNull();
        assertThat(result).isNotEmpty();
        assertThat(result.get(BufferPoolSampleCollector.SAMPLE_KEY)).isNotNull();
        assertThat(result.get(BufferPoolSampleCollector.SAMPLE_KEY) instanceof List).isTrue();
    }
}
