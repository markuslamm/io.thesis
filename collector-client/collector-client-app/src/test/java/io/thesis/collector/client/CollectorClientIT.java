package io.thesis.collector.client;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CollectorClientIT {

    @Autowired
    private CollectorClient collectorClient;

    @Test
    public void testGetMetadata() {
        final CompletableFuture<Map<String, Object>> metadataCP = collectorClient.getMetadata();
        assertThat(metadataCP).isNotNull();

        metadataCP.thenAccept(metadata -> {
            assertThat(metadata).isInstanceOf(Map.class);
            assertThat(metadata.get("hostname")).isNotNull();
            assertThat(metadata.get("instanceId")).isNotNull();
            assertThat(metadata.get("sourceSystem")).isNotNull();
            assertThat(metadata.get("registry")).isNotNull();
            assertThat(metadata.get("isRunning")).isNotNull();
        });
    }

}
