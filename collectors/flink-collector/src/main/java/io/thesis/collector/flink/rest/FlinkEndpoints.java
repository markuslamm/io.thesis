package io.thesis.collector.flink.rest;

/**
 * Constants of Apache Flink's HTTP endpoints the data will be collected from.
 */
public final class FlinkEndpoints {

    public static final String CONFIG = "/config";
    public static final String OVERVIEW = "/overview";
    public static final String JOBS = "/jobs";
    public static final String JOB_DETAILS = "/jobs/{jobId}";
    public static final String JOB_EXCEPTIONS = "/jobs/{jobId}/exceptions";
    public static final String JOB_CONFIG = "/jobs/{jobId}/config";

    private FlinkEndpoints() {}
}
