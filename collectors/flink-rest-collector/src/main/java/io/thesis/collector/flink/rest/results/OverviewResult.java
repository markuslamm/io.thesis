package io.thesis.collector.flink.rest.results;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class OverviewResult {

    private final  int taskManagers;
    private final int slotsTotal;
    private final int slotsAvailable;
    private final int jobsRunning;
    private final int jobsFinished;
    private final int jobsCancelled;
    private final int jobsFailed;

    @JsonCreator
    public OverviewResult(@JsonProperty("taskmanagers") final int taskManagers,
                          @JsonProperty("slots-total") final int slotsTotal,
                          @JsonProperty("slots-available") final int slotsAvailable,
                          @JsonProperty("jobs-running") final int jobsRunning,
                          @JsonProperty("jobs-finished") final int jobsFinished,
                          @JsonProperty("jobs-cancelled") final int jobsCancelled,
                          @JsonProperty("jobs-failed") final int jobsFailed) {
        this.taskManagers = taskManagers;
        this.slotsTotal = slotsTotal;
        this.slotsAvailable = slotsAvailable;
        this.jobsRunning = jobsRunning;
        this.jobsFinished = jobsFinished;
        this.jobsCancelled = jobsCancelled;
        this.jobsFailed = jobsFailed;
    }

    public int getTaskManagers() {
        return taskManagers;
    }

    public int getSlotsTotal() {
        return slotsTotal;
    }

    public int getSlotsAvailable() {
        return slotsAvailable;
    }

    public int getJobsRunning() {
        return jobsRunning;
    }

    public int getJobsFinished() {
        return jobsFinished;
    }

    public int getJobsCancelled() {
        return jobsCancelled;
    }

    public int getJobsFailed() {
        return jobsFailed;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
