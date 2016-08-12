package io.thesis.collector.flink.rest.results;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class JobConfigResult {

    private final String jid;
    private final String name;
    private final ExecutionConfig executionConfig;

    @JsonCreator
    public JobConfigResult(@JsonProperty("jid") final String jid,
                           @JsonProperty("name") final String name,
                           @JsonProperty("execution-config") final ExecutionConfig executionConfig) {
        this.jid = jid;
        this.name = name;
        this.executionConfig = executionConfig;
    }

    public String getJid() {
        return jid;
    }

    public String getName() {
        return name;
    }

    public ExecutionConfig getExecutionConfig() {
        return executionConfig;
    }

    public static class ExecutionConfig {

        private final String executionMode;
        private final String restartStrategy;
        private final int jobParallelism;
        private final boolean objectReuseMode;

        @JsonCreator
        private ExecutionConfig(@JsonProperty("execution-mode") final String executionMode,
                                @JsonProperty("restart-strategy") final String restartStrategy,
                                @JsonProperty("job-parallelism") final int jobParallelism,
                                @JsonProperty("object-reuse-mode") final boolean objectReuseMode) {
            this.executionMode = executionMode;
            this.restartStrategy = restartStrategy;
            this.jobParallelism = jobParallelism;
            this.objectReuseMode = objectReuseMode;
        }

        public String getExecutionMode() {
            return executionMode;
        }

        public String getRestartStrategy() {
            return restartStrategy;
        }

        public int getJobParallelism() {
            return jobParallelism;
        }

        public boolean isObjectReuseMode() {
            return objectReuseMode;
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
