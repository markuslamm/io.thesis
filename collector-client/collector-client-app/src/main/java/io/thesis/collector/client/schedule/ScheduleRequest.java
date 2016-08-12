package io.thesis.collector.client.schedule;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.validator.constraints.NotEmpty;

import static java.util.Objects.requireNonNull;

/**
 * JSON data container for a client schedule request.
 */
public class ScheduleRequest {

    @NotEmpty
    private final String action;
    private final Integer interval;

    @JsonCreator
    public ScheduleRequest(@JsonProperty("action") final String action,
                           @JsonProperty("interval") final Integer interval) {
        this.action = requireNonNull(action);
        this.interval = interval;
    }

    public String getAction() {
        return action;
    }

    public Integer getInterval() {
        return interval;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
