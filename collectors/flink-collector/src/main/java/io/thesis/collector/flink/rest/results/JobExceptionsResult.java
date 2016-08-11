package io.thesis.collector.flink.rest.results;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;

public class JobExceptionsResult {

    private final String rootException;
    private final List<ExceptionInfo> allExceptions;
    private final boolean truncated;

    @JsonCreator
    public JobExceptionsResult(@JsonProperty("root-exception") final String rootException,
                               @JsonProperty("all-exceptions") final List<ExceptionInfo> allExceptions,
                               @JsonProperty("truncated") final boolean truncated) {
        this.rootException = rootException;
        this.allExceptions = allExceptions;
        this.truncated = truncated;
    }

    public String getRootException() {
        return rootException;
    }

    public List<ExceptionInfo> getAllExceptions() {
        return allExceptions;
    }

    public boolean isTruncated() {
        return truncated;
    }

    private static class ExceptionInfo {
        private final String exception;
        private final String task;
        private final String location;

        @JsonCreator
        private ExceptionInfo(@JsonProperty("exception") final String exception,
                              @JsonProperty("task") final String task,
                              @JsonProperty("location") final String location) {
            this.exception = exception;
            this.task = task;
            this.location = location;
        }

        public String getException() {
            return exception;
        }

        public String getTask() {
            return task;
        }

        public String getLocation() {
            return location;
        }

        @Override
        public String toString() {
            return ToStringBuilder.reflectionToString(this);
        }
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
