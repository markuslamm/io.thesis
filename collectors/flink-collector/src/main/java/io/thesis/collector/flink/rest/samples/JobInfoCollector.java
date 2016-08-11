package io.thesis.collector.flink.rest.samples;

import com.google.common.collect.Maps;
import io.thesis.collector.flink.rest.FlinkRestClient;
import io.thesis.collector.flink.rest.results.JobConfigResult;
import io.thesis.collector.flink.rest.results.JobDetailResult;
import io.thesis.collector.flink.rest.results.JobExceptionsResult;
import io.thesis.collector.flink.rest.results.JobsResult;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

/**
 * Collects data from Apache Flinks's /jobs and aggregates job detail data.
 */
public final class JobInfoCollector extends AbstractFlinkRestSampleCollector {

    public static final String SAMPLE_KEY = "jobs";

    private static final String FLINK_JOB_CONFIG_MODE_KEY = "executionMode";
    private static final String FLINK_JOB_CONFIG_RESTART_STRATEGY_KEY = "restartStrategy";
    private static final String FLINK_JOB_CONFIG_JOB_PARALLELISM_KEY = "jobParallelism";
    private static final String FLINK_JOB_CONFIG_OBJECT_REUSE_MODE_KEY = "objectReuseMode";

    private static final String FLINK_JOB_NAME_KEY = "name";
    private static final String FLINK_JOB_STOPPABLE_KEY = "isStoppable";
    private static final String FLINK_JOB_STATE_KEY = "state";
    private static final String FLINK_JOB_START_TIME_KEY = "startTime";
    private static final String FLINK_JOB_END_TIME_KEY = "endTime";
    private static final String FLINK_JOB_DURATION_KEY = "duration";
    private static final String FLINK_JOB_NOW_KEY = "now";
    private static final String FLINK_JOB_TIMESTAMPS_PREFIX = "timestamps";

    private static final String FLINK_JOB_EXCEPTIONS_KEY = "exceptions";
    private static final String FLINK_JOB_VERTICES_KEY = "vertices";
    private static final String FLINK_JOB_STATUS_COUNTS_KEY = "statusCounts";
    private static final String FLINK_JOB_PLAN_KEY = "plan";

    public JobInfoCollector(final FlinkRestClient flinkRestClient) {
        super(requireNonNull(flinkRestClient));
    }

    @Override
    public CompletableFuture<Map<String, Object>> collectSample() {
        return aggregateJobInfo().thenApply(jobDataList -> {
            final Map<String, Object> resultMap = Maps.newLinkedHashMap();
            resultMap.put(SAMPLE_KEY, jobDataList);
            return resultMap;
        });
    }

    private CompletableFuture<List<Map<String, Object>>> aggregateJobInfo() {
        final CompletableFuture<JobsResult> jobsResultFuture = restClient().getJobs();
        final CompletableFuture<Set<String>> jobIdsFuture = jobsResultFuture.thenApply(JOB_IDS_FUNCTION);
        final CompletableFuture<List<CompletableFuture<Map<String, Object>>>> jobDetailsFutureListFuture =
                jobIdsFuture.thenApply(jobIds -> jobIds.stream().map(jobId -> {
                    final CompletableFuture<JobDetailResult> jobDetailsResultFuture = restClient().getJobDetails(jobId);
                    final CompletableFuture<JobExceptionsResult> jobExceptionsFuture = restClient().getJobExceptions(jobId);
                    final CompletableFuture<JobConfigResult> jobConfigFuture = restClient().getJobConfig(jobId);
                    return jobDetailsResultFuture.thenCompose(jobDetailResult ->
                            jobExceptionsFuture.thenCombine(jobConfigFuture, (jobJobExceptionsResult, jobConfigResult) -> {
                                final Map<String, Object> jobDataMap = new TreeMap<>();
                                final JobConfigResult.ExecutionConfig executionConfig = jobConfigResult.getExecutionConfig();
                                jobDataMap.put(FLINK_JOB_CONFIG_MODE_KEY, executionConfig.getExecutionMode());
                                jobDataMap.put(FLINK_JOB_CONFIG_RESTART_STRATEGY_KEY, executionConfig.getRestartStrategy());
                                jobDataMap.put(FLINK_JOB_CONFIG_JOB_PARALLELISM_KEY,executionConfig.getJobParallelism());
                                jobDataMap.put(FLINK_JOB_CONFIG_OBJECT_REUSE_MODE_KEY,executionConfig.isObjectReuseMode());
                                jobDataMap.put(FLINK_JOB_NAME_KEY, jobDetailResult.getName());
                                jobDataMap.put(FLINK_JOB_STOPPABLE_KEY, jobDetailResult.isStoppable());
                                jobDataMap.put(FLINK_JOB_STATE_KEY, jobDetailResult.getState());
                                jobDataMap.put(FLINK_JOB_START_TIME_KEY, jobDetailResult.getStartTime());
                                jobDataMap.put(FLINK_JOB_END_TIME_KEY, jobDetailResult.getEndTime());
                                jobDataMap.put(FLINK_JOB_DURATION_KEY, jobDetailResult.getDuration());
                                jobDataMap.put(FLINK_JOB_NOW_KEY, jobDetailResult.getNow());
                                jobDataMap.put(FLINK_JOB_EXCEPTIONS_KEY, jobJobExceptionsResult);
                                jobDataMap.put(FLINK_JOB_TIMESTAMPS_PREFIX, jobDetailResult.getTimestamps());
                                jobDataMap.put(FLINK_JOB_VERTICES_KEY, jobDetailResult.getVertices());
                                jobDataMap.put(FLINK_JOB_STATUS_COUNTS_KEY, jobDetailResult.getStatusCounts());
                                jobDataMap.put(FLINK_JOB_PLAN_KEY, jobDetailResult.getPlan());
                                return jobDataMap;
                            }));
                }).collect(Collectors.toList()));

        return jobDetailsFutureListFuture.thenCompose(jobDetailsFutures ->
                CompletableFuture.allOf(jobDetailsFutures.toArray(new CompletableFuture[jobDetailsFutures.size()]))
                        .thenApply(aVoid ->
                                jobDetailsFutures.stream()
                                        .map(CompletableFuture::join)
                                        .collect(Collectors.toList())));
    }

    private static final Function<JobsResult, Set<String>> JOB_IDS_FUNCTION = jobsResult -> {
        final Set<String> jobIds = new HashSet<>();
        jobIds.addAll(jobsResult.getRunning());
        jobIds.addAll(jobsResult.getFinished());
        jobIds.addAll(jobsResult.getCanceled());
        jobIds.addAll(jobsResult.getFailed());
        return jobIds;
    };
}

