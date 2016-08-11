package io.thesis.collector.manager.discovery;

import org.junit.Before;
import org.junit.Test;
import org.springframework.cloud.client.DefaultServiceInstance;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class CollectorClientInstanceServiceTest {

    private CollectorClientInstanceService service;
    private DiscoveryClient mockDiscoveryClient;
    private RestTemplate mockRestTemplate;

    @Before
    public void setUp() {
        mockDiscoveryClient = mock(DiscoveryClient.class);
        mockRestTemplate = mock(RestTemplate.class);
        service = new CollectorClientInstanceService(mockDiscoveryClient, mockRestTemplate, "collector-client");
    }

    @Test
    public void testGetClientInstances() {
        final List<ServiceInstance> serviceInstancesResult = Arrays.asList(
                new DefaultServiceInstance("serviceId", "1.2.3.4", 1234, false),
                new DefaultServiceInstance("serviceId", "1.2.3.5", 1235, false));
        given(mockDiscoveryClient.getInstances("collector-client")).willReturn(serviceInstancesResult);
        final CompletableFuture<List<CollectorClientInstance>> serviceInstancesCP = service.getClientInstances();
        assertThat(serviceInstancesCP).isNotNull();

        serviceInstancesCP.thenAccept(clientInstances -> {
            assertThat(clientInstances).isNotNull();
            assertThat(clientInstances).isNotEmpty();
            assertThat(clientInstances.size()).isEqualTo(2);
        });
    }
}
