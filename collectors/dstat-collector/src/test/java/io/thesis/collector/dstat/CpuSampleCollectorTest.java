package io.thesis.collector.dstat;

import io.thesis.collector.dstat.data.CpuSampleCollector;
import org.junit.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public final class CpuSampleCollectorTest extends AbstractDstatTest {

    private final CpuSampleCollector sampleCollector = new CpuSampleCollector();

    @Test
    public void testParse() {
        getInputData().forEach(s -> {
            final Map<String, Object> result = sampleCollector.apply(s);
            assertThat(result).isNotNull();
            assertThat(result.get(CpuSampleCollector.SAMPLE_KEY)).isNotNull();
        });
    }
}
