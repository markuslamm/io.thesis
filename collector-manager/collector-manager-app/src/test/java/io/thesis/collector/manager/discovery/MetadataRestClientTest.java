package io.thesis.collector.manager.discovery;

import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class MetadataRestClientTest {

    private RestTemplate mockRestTemplate;
    private MetadataRestClient metadataRestClient;

    @Before
    public void setUp() {
        mockRestTemplate = mock(RestTemplate.class);
        metadataRestClient = new MetadataRestClient(mockRestTemplate, "/client/metadata");
    }

    @Test
    @Ignore //TODO returns no response in RestTemplate mock. Why?
    public void testGetMetadata() {
        final CollectorMetadataResult metadataResult = new CollectorMetadataResult("instanceId", "hostname", "system",
                Lists.newArrayList(), Boolean.FALSE);
        final ResponseEntity<CollectorMetadataResult> responseEntity = new ResponseEntity<>(metadataResult, HttpStatus.OK);
        given(mockRestTemplate.exchange("http://1.2.3.4:1234/client/metadata", HttpMethod.GET, HttpEntity.EMPTY,
                CollectorMetadataResult.class))
                .willReturn(responseEntity);
        final CompletableFuture<CollectorMetadataResult> metadataResultCP = metadataRestClient.getMetadata("1.2.3.4", 1234);
        assertThat(metadataResultCP).isNotNull();
        final CollectorMetadataResult metadata = metadataResultCP.join();
        assertThat(metadata).isNotNull();
        assertThat(metadata.getRegistry()).isNotNull();
        assertThat(metadata.getRegistry()).isEmpty();
        assertThat(metadata.getHostname()).isNotNull();
        assertThat(metadata.getHostname()).isEqualTo("hostname");
        assertThat(metadata.getInstanceId()).isNotNull();
        assertThat(metadata.getInstanceId()).isEqualTo("instanceId");
        assertThat(metadata.getSystem()).isNotNull();
        assertThat(metadata.getSystem()).isEqualTo("system");
        assertThat(metadata.getIsRunning()).isFalse();
    }
}
