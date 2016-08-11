package io.thesis.collector.client;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CollectorClientIT {

    @Autowired
    private CollectorClient collectorClient;

    @Test
    public void testGetMetadata() {
        final Map<String, Object> result = collectorClient.getMetadata().join();
        assertThat(result).isNotNull();
        assertThat(result).isInstanceOf(Map.class);
        assertThat(result.get("hostname")).isNotNull();
        assertThat(result.get("instanceId")).isNotNull();
        assertThat(result.get("sourceSystem")).isNotNull();
        assertThat(result.get("registry")).isNotNull();
        assertThat(result.get("isRunning")).isNotNull();
    }

}
