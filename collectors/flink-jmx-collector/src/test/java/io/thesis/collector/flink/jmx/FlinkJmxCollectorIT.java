package io.thesis.collector.flink.jmx;

import io.thesis.collector.commons.CollectorResult;
import io.thesis.collector.commons.CollectorType;
import io.thesis.commons.json.JsonUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class FlinkJmxCollectorIT extends AbstractJmxIT {

    private static final String JMX_URL = "service:jmx:rmi:///jndi/rmi://localhost:9999/jmxrmi";

    private FlinkJmxCollector flinkJmxCollector;

    @Before
    public void setUp() throws IOException {
        flinkJmxCollector = new FlinkJmxCollector(mBeanServerConnection());
    }

    @Test
    public void testCollect() {
        final CollectorResult result = flinkJmxCollector.collect().join();
        System.err.println(JsonUtils.toJson(result));
        assertThat(result).isNotNull();
        assertThat(result.getClientTimestamp()).isNull();
        assertThat(result.getClientHost()).isNull();
        assertThat(result.getInstanceId()).isNull();
        assertThat(result.getCollectorType()).isNotNull();
        assertThat(result.getCollectorType()).isEqualTo(CollectorType.FLINK_JMX.name().toLowerCase());
        assertThat(result.getData()).isNotNull();
        assertThat(result.getData().isEmpty()).isFalse();
        assertThat(result.getData().get("jobmanager")).isNotNull();
    }

    @Override
    protected String getJmxUrl() {
        return JMX_URL;
    }
}
