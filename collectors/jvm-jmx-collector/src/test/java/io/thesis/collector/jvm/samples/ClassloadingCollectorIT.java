package io.thesis.collector.jvm.samples;

import io.thesis.collector.jvm.AbstractJmxIT;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class ClassloadingCollectorIT extends AbstractJmxIT {

    private ClassloadingSampleCollector classloadingSampleCollector;

    @Before
    public void setUp() throws IOException {
        classloadingSampleCollector = new ClassloadingSampleCollector(mBeanServerConnection());
    }

    @Test
    public void testCollectSample() {
        final Map<String, Object> result = classloadingSampleCollector.collectSample().join();
        assertThat(result).isNotNull();
        assertThat(result).isNotEmpty();
        assertThat(result.get(ClassloadingSampleCollector.SAMPLE_KEY)).isNotNull();
        assertThat(result.get(ClassloadingSampleCollector.SAMPLE_KEY) instanceof Map).isTrue();
    }
}
