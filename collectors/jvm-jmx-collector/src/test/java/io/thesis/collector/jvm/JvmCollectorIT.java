package io.thesis.collector.jvm;

import io.thesis.collector.commons.CollectorResult;
import io.thesis.collector.commons.CollectorType;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class JvmCollectorIT extends AbstractJmxIT {

    private JvmCollector jvmCollector;

    @Before
    public void setUp() throws IOException {
        jvmCollector = new JvmCollector(mBeanServerConnection());
    }

    @Test
    public void testCollect() {
        final CollectorResult result = jvmCollector.collect().join();
        assertThat(result).isNotNull();
        assertThat(result.getClientTimestamp()).isNull();
        assertThat(result.getClientHost()).isNull();
        assertThat(result.getInstanceId()).isNull();
        assertThat(result.getCollectorType()).isNotNull();
        assertThat(result.getData().isEmpty()).isFalse();
        assertThat(result.getData()).isNotNull();
        assertThat(result.getCollectorType()).isEqualTo(CollectorType.JVM_JMX.name().toLowerCase());
        assertThat(result.getData().get("classloading")).isNotNull();
        assertThat(result.getData().get("nio-buffer-pools")).isNotNull();
        assertThat(result.getData().get("garbage-collectors")).isNotNull();
        assertThat(result.getData().get("memory")).isNotNull();
        assertThat(result.getData().get("memory-pools")).isNotNull();
        assertThat(result.getData().get("runtime")).isNotNull();
        assertThat(result.getData().get("threads")).isNotNull();
        assertThat(result.getData().get("operating-system")).isNotNull();
    }
}
