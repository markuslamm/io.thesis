package io.thesis.collector.processor;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.time.LocalDateTime;
import java.util.Map;

import static java.util.Objects.requireNonNull;

public class CollectorResult {

    private final LocalDateTime clientTimestamp;
    private final String clientHost;
    private final Integer clientPort;
    private final String instanceId;
    private final String collectorType;
    private final Map<String, Object> data;

    @JsonCreator
    public CollectorResult(@JsonProperty("collectorType") final String collectorType,
                           @JsonProperty("data") final Map<String, Object> data,
                           @JsonProperty("clientTimestamp") final LocalDateTime clientTimestamp,
                           @JsonProperty("clientHost") final String clientHost,
                           @JsonProperty("clientPort") final Integer clientPort,
                           @JsonProperty("instanceId") final String instanceId) {
        this.collectorType = requireNonNull(collectorType);
        this.data = requireNonNull(data);
        this.clientTimestamp = clientTimestamp;
        this.clientHost = clientHost;
        this.clientPort = clientPort;
        this.instanceId = instanceId;
    }

    public LocalDateTime getClientTimestamp() {
        return clientTimestamp;
    }

    public String getClientHost() {
        return clientHost;
    }

    public Integer getClientPort() {
        return clientPort;
    }

    public String getCollectorType() {
        return collectorType;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
