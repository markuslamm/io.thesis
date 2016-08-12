package io.thesis.collector.dstat.util;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ConversionUtilTest {

    @Test
    public void testConvert() {
        assertThat(ConversionUtil.convertKilobytes("1000B")).isEqualTo(Double.valueOf("1000") / 1024);
        assertThat(ConversionUtil.convertKilobytes("1.1")).isEqualTo(1.1);
        assertThat(ConversionUtil.convertKilobytes("1000k")).isEqualTo(1000);
        assertThat(ConversionUtil.convertKilobytes("1000M")).isEqualTo(Double.valueOf("1000") * 1024);
        assertThat(ConversionUtil.convertKilobytes("1000G")).isEqualTo(Double.valueOf("1000") * 1024 * 1024);
    }
}
