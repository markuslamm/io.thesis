package io.thesis.collector.flink.rest;

import io.thesis.collector.flink.rest.results.*;

import java.util.concurrent.CompletableFuture;

/**
 * REST client for accessing Apache Flink's HTTP API
 */
public interface FlinkRestClient {

    /**
     * Performs an API call to '/config'
     *
     * @return a future with the result of the API call
     */
    CompletableFuture<ConfigResult> getFlinkConfig();

    /**
     * Performs an API call to '/overview'
     *
     * @return a future with the result of the API call
     */
    CompletableFuture<OverviewResult> getFlinkOverview();

    /**
     * Performs an API call to '/jobs'
     *
     * @return a future with the result of the API call
     */
    CompletableFuture<JobsResult> getJobs();

    /**
     * Performs an API call to '/jobs/{jobId}'
     *
     * @param jobId the unique identifier of the job
     * @return a future with the result of the API call
     */
    CompletableFuture<JobDetailResult> getJobDetails(String jobId);

    /**
     * Performs an API call to '/jobs/{jobId}/exceptions'
     *
     * @param jobId the unique identifier of the job
     * @return a future with the result of the API call
     */
    CompletableFuture<JobExceptionsResult> getJobExceptions(String jobId);

    /**
     * Performs an API call to '/jobs/{jobId}/config'
     *
     * @param jobId the unique identifier of the job
     * @return a future with the result of the API call
     */
    CompletableFuture<JobConfigResult> getJobConfig(String jobId);
}
