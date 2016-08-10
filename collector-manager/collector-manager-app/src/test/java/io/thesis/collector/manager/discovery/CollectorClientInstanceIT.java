package io.thesis.collector.manager.discovery;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CollectorClientInstanceIT {

    @Autowired
    private CollectorClientInstanceService service;

    @Test
    public void testGetClientInstances() {
        final List<CollectorClientInstance> serviceInstances = service.getClientInstances().join();
        assertThat(serviceInstances).isInstanceOf(List.class);
    }
}
