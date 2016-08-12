package io.thesis.collector.dstat;

import io.thesis.collector.commons.CollectorResult;
import io.thesis.collector.commons.CollectorType;
import io.thesis.commons.json.JsonUtils;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class DstatCollectorIT {

    private final DstatCollector dstatCollector = new DstatCollector();

    @Test
    public void testCollect() {
        final CollectorResult result = dstatCollector.collect().join();
        System.err.println(JsonUtils.toJson(result));
        assertThat(result).isNotNull();
        assertThat(result.getClientTimestamp()).isNull();
        assertThat(result.getClientHost()).isNull();
        assertThat(result.getInstanceId()).isNull();
        assertThat(result.getCollectorType()).isNotNull();
        assertThat(result.getData().isEmpty()).isFalse();
        assertThat(result.getData()).isNotNull();
        assertThat(result.getCollectorType()).isEqualTo(CollectorType.DSTAT.name().toLowerCase());
        assertThat(result.getData().get("cpu")).isNotNull();
//        assertThat(result.getData().get("disk")).isNotNull();
//        assertThat(result.getData().get("net")).isNotNull();
//        assertThat(result.getData().get("io")).isNotNull();
//        assertThat(result.getData().get("memory")).isNotNull();
//        assertThat(result.getData().get("system")).isNotNull();
//        assertThat(result.getData().get("process")).isNotNull();
    }
}
