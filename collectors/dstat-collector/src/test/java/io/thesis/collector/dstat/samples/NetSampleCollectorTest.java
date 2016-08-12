package io.thesis.collector.dstat.samples;

import io.thesis.collector.dstat.AbstractDstatTest;
import org.junit.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class NetSampleCollectorTest extends AbstractDstatTest {

    private final NetSampleCollector sampleCollector = new NetSampleCollector();

    @Test
    public void testParse() {
        getInputData().forEach(s -> {
            final Map<String, Object> result = sampleCollector.apply(s);
            assertThat(result).isNotNull();
            assertThat(result.get(NetSampleCollector.SAMPLE_KEY)).isNotNull();
        });
    }
}
