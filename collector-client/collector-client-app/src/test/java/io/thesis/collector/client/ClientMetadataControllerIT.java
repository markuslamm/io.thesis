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

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpMethod.GET;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ClientMetadataControllerIT {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testGetMetadata() {
        final ResponseEntity<CollectorMetadata> result = restTemplate.exchange("/client/metadata", GET,
                HttpEntity.EMPTY, new ParameterizedTypeReference<CollectorMetadata>() {
                });
        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        final CollectorMetadata metadata = result.getBody();
        assertThat(metadata).isNotNull();
        assertThat(metadata.getHostname()).isNotNull();
        assertThat(metadata.getInstanceId()).isNotNull();
        assertThat(metadata.getSystem()).isNotNull();
        assertThat(metadata.getRegistry()).isNotNull();
        assertThat(metadata.getRegistry()).isEmpty();
        assertThat(metadata.getIsRunning()).isNotNull();
    }
}
