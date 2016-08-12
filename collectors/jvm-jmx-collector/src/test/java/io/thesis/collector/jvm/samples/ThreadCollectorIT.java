package io.thesis.collector.jvm.samples;

import io.thesis.collector.jvm.AbstractJmxIT;
import io.thesis.commons.json.JsonUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class ThreadCollectorIT extends AbstractJmxIT {

    private ThreadSampleCollector threadSampleCollector;

    @Before
    public void setUp() throws IOException {
        threadSampleCollector = new ThreadSampleCollector(mBeanServerConnection());
    }

    @Test
    public void testCollectSample() {
        final Map<String, Object> result = threadSampleCollector.collectSample().join();
        System.err.println(JsonUtils.toJson(result));
        assertThat(result).isNotNull();
        assertThat(result).isNotEmpty();
        assertThat(result.get(ThreadSampleCollector.SAMPLE_KEY)).isNotNull();
        assertThat(result.get(ThreadSampleCollector.SAMPLE_KEY) instanceof Map).isTrue();
    }
}
