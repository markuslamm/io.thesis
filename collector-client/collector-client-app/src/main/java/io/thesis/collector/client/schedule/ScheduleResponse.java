package io.thesis.collector.client.schedule;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.Objects.requireNonNull;

/**
 * JSON data container for a clients schedule respones.
 */
public class ScheduleResponse {

    private final String message;
    private final Boolean isRunning;

    private final LocalDateTime startTime;
    private final List<String> registeredCollectors;

    public ScheduleResponse(final String message, final Boolean isRunning, final LocalDateTime startTime,
                            final List<String> collectors) {
        this.message = requireNonNull(message);
        this.isRunning = requireNonNull(isRunning);
        this.startTime = requireNonNull(startTime);
        registeredCollectors = requireNonNull(collectors);
    }

    public String getMessage() {
        return message;
    }

    public Boolean getIsRunning() {
        return isRunning;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public List<String> getRegisteredCollectors() {
        return registeredCollectors;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
