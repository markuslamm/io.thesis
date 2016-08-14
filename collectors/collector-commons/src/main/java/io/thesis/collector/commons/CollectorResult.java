package io.thesis.collector.commons;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.time.LocalDateTime;
import java.util.Map;

import static java.util.Objects.requireNonNull;

/**
 * Container for the result of a {@code Collector} implementation, contains additional metadata about the client
 * and collection time.
 */
public class CollectorResult {

    @JsonProperty("client-timestamp")
    private final LocalDateTime clientTimestamp;

    @JsonProperty("client-host")
    private final String clientHost;

    @JsonProperty("client-port")
    private final Integer clientPort;

    @JsonProperty("instance-id")
    private final String instanceId;

    @JsonProperty("collector-type")
    private final String collectorType;

    private final Map<String, Object> data;

    public CollectorResult(final String collectorType, final Map<String, Object> data, final LocalDateTime clientTimestamp,
                           final String clientHost, final Integer clientPort, final String instanceId) {
        this.collectorType = requireNonNull(collectorType);
        this.data = requireNonNull(data);
        this.clientTimestamp = clientTimestamp;
        this.clientHost = clientHost;
        this.clientPort = clientPort;
        this.instanceId = instanceId;
    }

    public CollectorResult(final String collectorType, final Map<String, Object> data) {
        this(collectorType, data, null, null, null, null);
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
