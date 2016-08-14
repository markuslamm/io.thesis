package io.thesis.collector.processor;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.time.LocalDateTime;
import java.util.Map;

import static java.util.Objects.requireNonNull;

public class CollectorResult {

    private final Object clientTimestamp;
    private final String clientHost;
    private final Integer clientPort;
    private final String instanceId;
    private final String collectorType;
    private final Map<String, Object> data;

    @JsonCreator
    public CollectorResult(@JsonProperty("collectorType") final String collectorType,
                           @JsonProperty("data") final Map<String, Object> data,
                           @JsonProperty("client-timestamp") final Object clientTimestamp,
                           @JsonProperty("client-host") final String clientHost,
                           @JsonProperty("client-port") final Integer clientPort,
                           @JsonProperty("instance-id") final String instanceId) {
        this.collectorType = requireNonNull(collectorType);
        this.data = requireNonNull(data);
        this.clientTimestamp = clientTimestamp;
        this.clientHost = clientHost;
        this.clientPort = clientPort;
        this.instanceId = instanceId;
    }

    public Object getClientTimestamp() {
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
