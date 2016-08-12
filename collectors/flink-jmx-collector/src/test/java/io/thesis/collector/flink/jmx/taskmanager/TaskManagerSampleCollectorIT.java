package io.thesis.collector.flink.jmx.taskmanager;

import io.thesis.collector.flink.jmx.AbstractJmxIT;
import io.thesis.commons.json.JsonUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class TaskManagerSampleCollectorIT extends AbstractJmxIT {

    private static final String JMX_URL = "service:jmx:rmi:///jndi/rmi://localhost:9998/jmxrmi";

    private TaskManagerSampleCollector taskManagerSampleCollector;

    @Before
    public void setUp() throws IOException {
        taskManagerSampleCollector = new TaskManagerSampleCollector(mBeanServerConnection());
    }

    @Test
    public void testCollectSample() {
        final Map<String, Object> result = taskManagerSampleCollector.collectSample().join();
        System.err.println(JsonUtils.toJson(result));
        assertThat(result).isNotNull();
        assertThat(result).isNotEmpty();
        assertThat(result.get(TaskManagerSampleCollector.SAMPLE_KEY)).isNotNull();
        assertThat(result.get(TaskManagerSampleCollector.SAMPLE_KEY) instanceof List).isTrue();
    }

    @Override
    protected String getJmxUrl() {
        return JMX_URL;
    }
}
