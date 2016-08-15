package io.thesis.collector.flink.rest.samples;

import io.thesis.collector.flink.rest.client.FlinkRestClient;
import io.thesis.collector.flink.rest.client.FlinkRestClientImpl;
import org.junit.Before;
import org.junit.Test;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public final class JobInfoCollectorIT {

    private JobInfoCollector collector;

    @Before
    public void setUp() {
        final FlinkRestClient flinkRestClient = new FlinkRestClientImpl(new RestTemplate(), "localhost", 8081);
        collector = new JobInfoCollector(flinkRestClient);

    }
    @Test
    public void testCollectSample() {
        final Map<String, Object> result = collector.collectSample().join();
        assertThat(result).isNotNull();
        assertThat(result.get(JobInfoCollector.SAMPLE_KEY)).isNotNull();
    }
}
