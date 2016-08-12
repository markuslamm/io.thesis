package io.thesis.collector.flink.rest;

import io.thesis.collector.commons.CollectorResult;
import io.thesis.collector.commons.CollectorType;
import io.thesis.collector.flink.FlinkRestCollector;
import io.thesis.commons.json.JsonUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThat;

public class FlinkCollectorIT {

    private FlinkRestCollector collector;

    @Before
    public void setUp() throws IOException {
        final FlinkRestClient flinkRestClient = new FlinkRestClientImpl(new RestTemplate(), "localhost", 8081);
        collector = new FlinkRestCollector(flinkRestClient);
    }

    @Test
    public void testCollectSample() {
        final CompletableFuture<CollectorResult> resultCP = collector.collect();
        assertThat(resultCP).isNotNull();
        final CollectorResult result = resultCP.join();
        assertThat(result).isNotNull();
        assertThat(result.getClientTimestamp()).isNull();
        assertThat(result.getClientHost()).isNull();
        assertThat(result.getInstanceId()).isNull();
        assertThat(result.getCollectorType()).isNotNull();
        assertThat(result.getData().isEmpty()).isFalse();
        assertThat(result.getData()).isNotNull();
        assertThat(result.getCollectorType()).isEqualTo(CollectorType.FLINK.fullText());
        assertThat(result.getData().get("cluster")).isNotNull();
        assertThat(result.getData().get("jobs")).isNotNull();
        System.err.println(JsonUtils.toJson(result));
    }
}
