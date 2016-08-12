package io.thesis.collector.dstat.samples;

import io.thesis.collector.dstat.AbstractDstatTest;
import org.junit.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class DiskSampleCollectorTest extends AbstractDstatTest {

    private final DiskSampleCollector sampleCollector = new DiskSampleCollector();

    @Test
    public void testParse() {
        getInputData().forEach(s -> {
            final Map<String, Object> result = sampleCollector.apply(s);
            assertThat(result).isNotNull();
            assertThat(result.get(DiskSampleCollector.SAMPLE_KEY)).isNotNull();
        });
    }
}
