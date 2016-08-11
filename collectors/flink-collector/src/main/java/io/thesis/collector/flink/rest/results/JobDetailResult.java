package io.thesis.collector.flink.rest.results;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;
import java.util.Map;

public final class JobDetailResult {

    private final String jid;
    private final String name;
    private final boolean isStoppable;
    private final String state;
    private final long startTime;
    private final long endTime;
    private final long duration;
    private final long now;
    private final Map<String, Long> timestamps;
    private final List<Vertex> vertices;
    private final Map<String, Integer> statusCounts;
    private final Object plan;

    @JsonCreator
    public JobDetailResult(@JsonProperty("jid") final String jid,
                           @JsonProperty("name") final String name,
                           @JsonProperty("isStoppable") final boolean isStoppable,
                           @JsonProperty("state") final String state,
                           @JsonProperty("start-time") final long startTime,
                           @JsonProperty("end-time") final long endTime,
                           @JsonProperty("duration") final long duration,
                           @JsonProperty("now") final long now,
                           @JsonProperty("timestamps") final Map<String, Long> timestamps,
                           @JsonProperty("vertices") final List<Vertex> vertices,
                           @JsonProperty("status-counts") final Map<String, Integer> statusCounts,
                           @JsonProperty("plan") final Object plan) {
        super();
        this.jid = jid;
        this.name = name;
        this.isStoppable = isStoppable;
        this.state = state;
        this.startTime = startTime;
        this.endTime = endTime;
        this.duration = duration;
        this.now = now;
        this.timestamps = timestamps;
        this.vertices = vertices;
        this.statusCounts = statusCounts;
        this.plan = plan;
    }

    public String getJid() {
        return jid;
    }

    public String getName() {
        return name;
    }

    public boolean isStoppable() {
        return isStoppable;
    }

    public String getState() {
        return state;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public long getDuration() {
        return duration;
    }

    public long getNow() {
        return now;
    }

    public Map<String, Long> getTimestamps() {
        return timestamps;
    }

    public List<Vertex> getVertices() {
        return vertices;
    }

    public Map<String, Integer> getStatusCounts() {
        return statusCounts;
    }

    public Object getPlan() {
        return plan;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    public static class Vertex {

        private final String id;
        private final String name;
        private final int parallelism;
        private final String status;
        private final long startTime;
        private final long endTime;
        private final long duration;
        private final Map<String, Integer> tasks;
        private final Map<String, Integer> metrics;

        @JsonCreator
        private Vertex(@JsonProperty("id") final String id,
                        @JsonProperty("name") final String name,
                        @JsonProperty("parallelism") final int parallelism,
                       @JsonProperty("status") final String status,
                        @JsonProperty("start-time") final long startTime,
                        @JsonProperty("end-time") final long endTime,
                        @JsonProperty("duration") final long duration,
                        @JsonProperty("tasks") final Map<String, Integer> tasks,
                        @JsonProperty("metrics") final Map<String, Integer> metrics) {
            this.id = id;
            this.name = name;
            this.parallelism = parallelism;
            this.status = status;
            this.startTime = startTime;
            this.endTime = endTime;
            this.duration = duration;
            this.tasks = tasks;
            this.metrics = metrics;
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public int getParallelism() {
            return parallelism;
        }

        public String getStatus() {
            return status;
        }

        public long getStartTime() {
            return startTime;
        }

        public long getEndTime() {
            return endTime;
        }

        public long getDuration() {
            return duration;
        }

        public Map<String, Integer> getTasks() {
            return tasks;
        }

        public Map<String, Integer> getMetrics() {
            return metrics;
        }

        @Override
        public String toString() {
            return ToStringBuilder.reflectionToString(this);
        }
    }
}
