package io.thesis.collector.flink.rest.client;


import io.thesis.collector.commons.rest.AbstractRestClient;
import io.thesis.collector.flink.rest.results.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.CompletableFuture;

import static java.util.Objects.requireNonNull;

/**
 * Implementation of {@code FlinkRestClient} based on Spring's {@code RestTemplate}
 */
public final class FlinkRestClientImpl extends AbstractRestClient implements FlinkRestClient {

    public FlinkRestClientImpl(final RestTemplate restTemplate, final String flinkHost, final Integer flinkPort) {
        super(requireNonNull(restTemplate), requireNonNull(flinkHost), requireNonNull(flinkPort));
    }

    @Override
    public CompletableFuture<ConfigResult> getFlinkConfig() {
        return CompletableFuture.supplyAsync(() -> {
            final ResponseEntity<ConfigResult> apiResponse = executeGet(FlinkEndpoints.CONFIG,
                    ConfigResult.class);
            return apiResponse.getBody();
        });
    }

    @Override
    public CompletableFuture<OverviewResult> getFlinkOverview() {
        return CompletableFuture.supplyAsync(() -> {
            final ResponseEntity<OverviewResult> apiResponse = executeGet(FlinkEndpoints.OVERVIEW,
                    OverviewResult.class);
            return apiResponse.getBody();
        });
    }

    @Override
    public CompletableFuture<JobsResult> getJobs() {
        return CompletableFuture.supplyAsync(() -> {
            final ResponseEntity<JobsResult> apiResponse = executeGet(FlinkEndpoints.JOBS,
                    JobsResult.class);
            return apiResponse.getBody();
        });
    }

    @Override
    public CompletableFuture<JobDetailResult> getJobDetails(final String jobId) {
        return CompletableFuture.supplyAsync(() -> {
            final ResponseEntity<JobDetailResult> apiResponse = executeGet(FlinkEndpoints.JOB_DETAILS,
                    JobDetailResult.class, jobId);
            return apiResponse.getBody();
        });
    }

    @Override
    public CompletableFuture<JobExceptionsResult> getJobExceptions(final String jobId) {
        return CompletableFuture.supplyAsync(() -> {
            final ResponseEntity<JobExceptionsResult> apiResponse = executeGet(FlinkEndpoints.JOB_EXCEPTIONS,
                    JobExceptionsResult.class, jobId);
            return apiResponse.getBody();
        });
    }

    @Override
    public CompletableFuture<JobConfigResult> getJobConfig(final String jobId) {
        return CompletableFuture.supplyAsync(() -> {
            final ResponseEntity<JobConfigResult> apiResponse = executeGet(FlinkEndpoints.JOB_CONFIG,
                    JobConfigResult.class, jobId);
            return apiResponse.getBody();
        });
    }
}
