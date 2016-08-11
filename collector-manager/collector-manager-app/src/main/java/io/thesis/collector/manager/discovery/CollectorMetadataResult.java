package io.thesis.collector.manager.discovery;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.List;

/**
 * Wrapper class for the result of the request of collector-clients '/metadata endpoint.
 */
public class CollectorMetadataResult  {

    private static final String UNKNOWN_KEY = "unknown";

    private final String instanceId;
    private final String hostname;
    private final List<String> registry;
    private final Boolean isRunning;

    @JsonCreator
    public CollectorMetadataResult(@JsonProperty("instanceId") final String instanceId,
                                   @JsonProperty("hostname") final String hostname,
                                   @JsonProperty("registry") final List<String> registry,
                                   @JsonProperty("isRunning") final Boolean isRunning) {
        this.instanceId = instanceId;
        this.hostname = hostname;
        this.registry = registry;
        this.isRunning = isRunning;
    }

    public CollectorMetadataResult() {
        this(UNKNOWN_KEY, UNKNOWN_KEY, Lists.newArrayList(), null);
    }

    public String getInstanceId() {
        return instanceId;
    }

    public String getHostname() {
        return hostname;
    }

    public List<String> getRegistry() {
        return registry;
    }

    public Boolean getIsRunning() {
        return isRunning;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
