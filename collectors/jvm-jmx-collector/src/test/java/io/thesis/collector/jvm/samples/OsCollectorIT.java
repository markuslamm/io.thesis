package io.thesis.collector.jvm.samples;

import io.thesis.collector.jvm.AbstractJmxIT;
import io.thesis.commons.json.JsonUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class OsCollectorIT extends AbstractJmxIT {

    private OsSampleCollector osSampleCollector;

    @Before
    public void setUp() throws IOException {
        osSampleCollector = new OsSampleCollector(mBeanServerConnection());
    }

    @Test
    public void testCollectSample() {
        final Map<String, Object> result = osSampleCollector.collectSample().join();

        System.err.println(JsonUtils.toJson(result));
        assertThat(result).isNotNull();
        assertThat(result).isNotEmpty();
        assertThat(result.get(OsSampleCollector.SAMPLE_KEY)).isNotNull();
        assertThat(result.get(OsSampleCollector.SAMPLE_KEY) instanceof Map).isTrue();
    }
}
