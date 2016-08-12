package io.thesis.collector.flink.rest.results;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class ConfigResult {

    private final int refreshInterval;
    private final int timezoneOffset;
    private final String timezoneName;
    private final String flinkVersion;
    private final String flinkRevision;

    @JsonCreator
    public ConfigResult(@JsonProperty("refresh-interval") final int refreshInterval,
                        @JsonProperty("timezone-offset") final int timezoneOffset,
                        @JsonProperty("timezone-name") final String timezoneName,
                        @JsonProperty("flink-version") final String flinkVersion,
                        @JsonProperty("flink-revision") final String flinkRevision) {
        this.refreshInterval = refreshInterval;
        this.timezoneOffset = timezoneOffset;
        this.timezoneName = timezoneName;
        this.flinkVersion = flinkVersion;
        this.flinkRevision = flinkRevision;
    }

    public int getRefreshInterval() {
        return refreshInterval;
    }

    public int getTimezoneOffset() {
        return timezoneOffset;
    }

    public String getTimezoneName() {
        return timezoneName;
    }

    public String getFlinkVersion() {
        return flinkVersion;
    }

    public String getFlinkRevision() {
        return flinkRevision;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
