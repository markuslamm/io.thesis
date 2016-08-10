package io.thesis.collector.manager.discovery;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.client.DefaultServiceInstance;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest
public class CollectorClientInstanceServiceTest {

    @Autowired
    private CollectorClientInstanceService service;

    @MockBean
    private DiscoveryClient mockDiscoveryClient;

    @MockBean
    private RestTemplate mockRestTemplate;

    @Test
    public void testGetClientInstances() {
        final List<ServiceInstance> serviceInstancesResult = Arrays.asList(
                new DefaultServiceInstance("serviceId", "1.2.3.4", 1234, false),
                new DefaultServiceInstance("serviceId", "1.2.3.5", 1235, false));
        given(this.mockDiscoveryClient.getInstances("collector-server")).willReturn(serviceInstancesResult);
        final List<CollectorClientInstance> serviceInstances = service.getClientInstances().join();
        assertThat(serviceInstances).isNotNull();
        assertThat(serviceInstances).isNotEmpty();
        assertThat(serviceInstances.size()).isEqualTo(2);
    }
}
