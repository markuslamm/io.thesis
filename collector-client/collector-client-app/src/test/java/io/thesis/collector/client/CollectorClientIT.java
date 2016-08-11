package io.thesis.collector.client;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CollectorClientIT {

    @Autowired
    private CollectorClient collectorClient;

    @Test
    public void testGetMetadata() {
        final CompletableFuture<CollectorMetadata> metadataCP = collectorClient.getMetadata();
        assertThat(metadataCP).isNotNull();
        final CollectorMetadata metadata = metadataCP.join();
        assertThat(metadata.getHostname()).isNotNull();
        assertThat(metadata.getInstanceId()).isNotNull();
        assertThat(metadata.getSystem()).isNotNull();
        assertThat(metadata.getRegistry()).isNotNull();
        assertThat(metadata.getRegistry()).isEmpty();
        assertThat(metadata.getIsRunning()).isNotNull();
    }
}
