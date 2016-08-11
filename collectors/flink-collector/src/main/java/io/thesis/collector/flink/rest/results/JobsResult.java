package io.thesis.collector.flink.rest.results;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;

public final class JobsResult {

    private final List<String> running;
    private final List<String> finished;
    private final List<String> canceled;
    private final List<String> failed;

    @JsonCreator
    public JobsResult(@JsonProperty("jobs-running") final List<String> running,
                      @JsonProperty("jobs-finished") final List<String> finished,
                      @JsonProperty("jobs-cancelled") final List<String> canceled,
                      @JsonProperty("jobs-failed") final List<String> failed) {
        super();
        this.running = running;
        this.finished = finished;
        this.canceled = canceled;
        this.failed = failed;
    }

    public List<String> getRunning() {
        return running;
    }

    public List<String> getFinished() {
        return finished;
    }

    public List<String> getCanceled() {
        return canceled;
    }

    public List<String> getFailed() {
        return failed;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
