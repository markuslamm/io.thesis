package io.thesis.collector.client.ui;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpMethod.GET;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class IndexControllerIT {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testIndexPage() {
        final ResponseEntity<String> result = restTemplate.exchange("/", GET, HttpEntity.EMPTY, String.class);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
