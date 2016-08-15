package io.thesis.collector.flink.jmx.samples;

import io.thesis.collector.flink.jmx.AbstractJmxIT;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class TaskManagerSampleCollectorIT extends AbstractJmxIT {

    private static final String JMX_URL = "service:jmx:rmi:///jndi/rmi://localhost:9998/jmxrmi";

    private TaskManagerSampleCollector taskManagerSampleCollector;

    @Before
    public void setUp() throws IOException {
        taskManagerSampleCollector = new TaskManagerSampleCollector(mBeanServerConnection());
    }

    @Test
    public void testCollectSample() {
        Assertions.assertThatThrownBy(() -> {
            taskManagerSampleCollector.collectSample();
        }).isInstanceOf(UnsupportedOperationException.class);
    }

    @Override
    protected String getJmxUrl() {
        return JMX_URL;
    }
}
