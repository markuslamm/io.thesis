package io.thesis.collector.jvm.samples;

import io.thesis.collector.jvm.AbstractJmxIT;
import io.thesis.commons.json.JsonUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class GcCollectorIT extends AbstractJmxIT {

    private GcSampleCollector gcSampleCollector;

    @Before
    public void setUp() throws IOException {
        gcSampleCollector = new GcSampleCollector(mBeanServerConnection());
    }

    @Test
    public void testCollectSample() {
        final Map<String, Object> result = gcSampleCollector.collectSample().join();
        System.err.println(JsonUtils.toJson(result));
        assertThat(result).isNotNull();
        assertThat(result).isNotEmpty();
        assertThat(result.get(GcSampleCollector.SAMPLE_KEY)).isNotNull();
        assertThat(result.get(GcSampleCollector.SAMPLE_KEY) instanceof List).isTrue();
    }
}
