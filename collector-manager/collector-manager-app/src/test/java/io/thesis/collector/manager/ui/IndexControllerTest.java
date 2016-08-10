package io.thesis.collector.manager.ui;

import io.thesis.collector.manager.discovery.CollectorClientInstance;
import io.thesis.collector.manager.discovery.CollectorClientInstanceService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@WebMvcTest(IndexController.class)
public class IndexControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private CollectorClientInstanceService clientInstanceService;

    @Test
    public void testExample() throws Exception {
        final CompletableFuture<List<CollectorClientInstance>> resultCP =
                CompletableFuture.supplyAsync(() -> Arrays.asList(
                        CollectorClientInstance.of("1.2.3.4", 1234, "serviceId", false,
                                UriComponentsBuilder.fromHttpUrl("http://1.2.3.4:1234").build().toUri()),
                        CollectorClientInstance.of("1.2.3.5", 1235, "serviceId", false,
                                UriComponentsBuilder.fromHttpUrl("http://1.2.3.5:1235").build().toUri())));
        given(clientInstanceService.getClientInstances()).willReturn(resultCP);

        mvc.perform(get("/").accept(MediaType.TEXT_PLAIN)).andExpect(status().isOk());
    }
}
