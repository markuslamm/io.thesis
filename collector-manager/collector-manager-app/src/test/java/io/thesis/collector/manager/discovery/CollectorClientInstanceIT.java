package io.thesis.collector.manager.discovery;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CollectorClientInstanceIT {

    @Autowired
    private CollectorClientInstanceService service;

    @Test
    public void testGetClientInstances() {
        final CompletableFuture<List<CollectorClientInstance>> serviceInstancesCP = service.getClientInstances();
        assertThat(serviceInstancesCP).isNotNull();

        serviceInstancesCP.thenAccept(clientInstances -> {
            assertThat(clientInstances).isNotNull();
            assertThat(clientInstances).isInstanceOf(List.class);
        });
    }
}
