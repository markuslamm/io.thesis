package io.thesis.collector.client;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.List;

/**
 * Data container for metadata of the {@code collector-client}.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CollectorMetadata {

    private final String instanceId;
    private final String hostname;
    private final String system;
    private final List<String> collectorRegistry;
    private final Boolean isRunning;

    @JsonCreator
    public CollectorMetadata(@JsonProperty("instanceId") final String instanceId,
                             @JsonProperty("hostname") final String hostname,
                             @JsonProperty("sourceSystem") final String system,
                             @JsonProperty("registry") final List<String> collectorRegistry,
                             @JsonProperty("isRunning") final Boolean isRunning) {
        this.instanceId = instanceId;
        this.hostname = hostname;
        this.system = system;
        this.collectorRegistry = collectorRegistry;
        this.isRunning = isRunning;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public String getHostname() {
        return hostname;
    }

    public String getSystem() {
        return system;
    }

    public List<String> getCollectorRegistry() {
        return collectorRegistry;
    }

    public Boolean getIsRunning() {
        return isRunning;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
