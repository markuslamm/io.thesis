package io.thesis.collector.client;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpMethod.GET;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ClientMetadataControllerIT {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testGetMetadata() {
        final ResponseEntity<CompletableFuture<CollectorMetadata>> result = restTemplate.exchange("/client/metadata", GET,
                HttpEntity.EMPTY, new ParameterizedTypeReference<CompletableFuture<CollectorMetadata>>() {
                });
        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        final CompletableFuture<CollectorMetadata> metadataCP = result.getBody();
        assertThat(metadataCP).isNotNull();
        metadataCP.thenAccept(metadata -> {
            assertThat(metadata).isNotNull();
            assertThat(metadata.getCollectorRegistry()).isNotNull();
            assertThat(metadata.getInstanceId()).isNotNull();
            assertThat(metadata.getSystem()).isNotNull();
            assertThat(metadata.getHostname()).isNotNull();
            assertThat(metadata.getIsRunning()).isNotNull();
        });
    }
}
