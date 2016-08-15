package io.thesis.collector.flink.rest.samples;

import com.google.common.collect.Maps;
import io.thesis.collector.flink.rest.client.FlinkRestClient;
import io.thesis.collector.flink.rest.results.ConfigResult;
import io.thesis.collector.flink.rest.results.OverviewResult;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static java.util.Objects.requireNonNull;

/**
 * Collects data from Apache Flinks's /cluster and /overview endpoints.
 */
public final class ClusterInfoCollector extends AbstractFlinkRestSampleCollector {

    public static final String SAMPLE_KEY = "cluster";
    private static final String FLINK_TIMEZONE_NAME_KEY = "timezone-name";
    private static final String FLINK_TIMEZONE_OFFSET_KEY = "timezone-offset";
    private static final String FLINK_VERSION_KEY = "version";
    private static final String FLINK_REVISION_KEY = "revision";
    private static final String FLINK_TASKMANAGERS_KEY = "taskManagers";
    private static final String FLINK_SLOTS_TOTAL_KEY = "slots-total";
    private static final String FLINK_SLOTS_AVAILABLE_KEY = "slots-available";
    private static final String FLINK_JOBS_RUNNING_KEY = "jobs-running";
    private static final String FLINK_FINISHED_KEY = "jobs-finished";
    private static final String FLINK_CANCELLED_KEY = "jobs-cancelled";
    private static final String FLINK_FAILED_KEY = "jobs-failed";

    public ClusterInfoCollector(final FlinkRestClient flinkRestClient) {
        super(requireNonNull(flinkRestClient));
    }

    @Override
    public CompletableFuture<Map<String, Object>> collectSample() {
        final CompletableFuture<ConfigResult> flinkConfigFuture = restClient().getFlinkConfig();
        final CompletableFuture<OverviewResult> flinkOverviewFuture = restClient().getFlinkOverview();
        return flinkConfigFuture.thenCombine(flinkOverviewFuture, (flinkConfig, flinkOverview) -> {
            final Map<String, Object> dataMap = Maps.newLinkedHashMap();
            dataMap.put(FLINK_TIMEZONE_NAME_KEY, flinkConfig.getTimezoneName());
            dataMap.put(FLINK_TIMEZONE_OFFSET_KEY, flinkConfig.getTimezoneOffset());
            dataMap.put(FLINK_VERSION_KEY, flinkConfig.getFlinkVersion());
            dataMap.put(FLINK_REVISION_KEY, flinkConfig.getFlinkRevision());
            dataMap.put(FLINK_TASKMANAGERS_KEY, flinkOverview.getTaskManagers());
            dataMap.put(FLINK_SLOTS_TOTAL_KEY, flinkOverview.getSlotsTotal());
            dataMap.put(FLINK_SLOTS_AVAILABLE_KEY, flinkOverview.getSlotsAvailable());
            dataMap.put(FLINK_JOBS_RUNNING_KEY, flinkOverview.getJobsRunning());
            dataMap.put(FLINK_FINISHED_KEY, flinkOverview.getJobsFinished());
            dataMap.put(FLINK_CANCELLED_KEY, flinkOverview.getJobsCancelled());
            dataMap.put(FLINK_FAILED_KEY, flinkOverview.getJobsFailed());
            final Map<String, Object> resultMap = Maps.newLinkedHashMap();
            resultMap.put(SAMPLE_KEY, dataMap);
            return resultMap;
        });
    }
}
