package io.thesis.collector.client;

import io.thesis.collector.client.outbound.OutboundWriter;
import io.thesis.collector.client.schedule.ScheduleRequest;
import io.thesis.collector.client.schedule.ScheduleResponse;
import io.thesis.collector.commons.CollectorType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.TaskScheduler;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ScheduledFuture;
import java.util.stream.Collectors;

import static java.time.LocalDateTime.now;
import static java.util.Objects.requireNonNull;

/**
 * The main component for collecting data from source systems the client is installed on.
 * It manages registered collectors in a {@code CollectorRegistry} and the scheduling of user-triggered
 * data collection.
 */
public class CollectorClient {

    private static final Logger LOG = LoggerFactory.getLogger(CollectorClient.class);

    private final CollectorRegistry collectorRegistry;
    private final OutboundWriter outboundWriter;
    private final TaskScheduler taskScheduler;

    private final String instanceId;
    private final Integer clientPort;
    private final Integer defaultIntervalInMs;

    private List<ScheduledFuture<?>> collectorTaskFutures = null;

    private volatile boolean isRunning = false;

    public CollectorClient(final CollectorRegistry collectorRegistry, final OutboundWriter outboundWriter,
                           final TaskScheduler taskScheduler, final String instanceId, final Integer clientPort,
                           @Value("${collector.client.default-interval-in-ms}") final Integer defaultIntervalInMs) {
        this.collectorRegistry = requireNonNull(collectorRegistry);
        this.outboundWriter = requireNonNull(outboundWriter);
        this.taskScheduler = requireNonNull(taskScheduler);
        this.instanceId = requireNonNull(instanceId);
        this.clientPort = requireNonNull(clientPort);
        this.defaultIntervalInMs = requireNonNull(defaultIntervalInMs);
    }

    /**
     * Schedule the data collection process, based on incoming {@code ScheduleRequest} data..
     *
     * @param scheduleRequest required schedule request data
     * @return a future with {@code ScheduleResponse} data of the schedule request
     */
    public CompletableFuture<ScheduleResponse> scheduleClient(final ScheduleRequest scheduleRequest) {
        LOG.debug("Entering scheduleClient()");
        final CompletableFuture<ScheduleResponse> scheduleResponseCF = CompletableFuture.supplyAsync(() -> {
            final String action = requireNonNull(scheduleRequest.getAction());
            final CompletableFuture<ScheduleResponse> scheduleResponseFuture;
            switch (action) {
                case "start":
                    LOG.debug("Start scheduling collector-client, action='{}', interval='{}'", scheduleRequest.getAction(),
                            scheduleRequest.getInterval());
                    final Integer interval = Optional.ofNullable(scheduleRequest.getInterval()).orElse(defaultIntervalInMs);
                    scheduleResponseFuture = startCollect(interval);
                    break;
                case "stop":
                    LOG.debug("Stop scheduling collector-client, action='{}'", scheduleRequest.getAction());
                    scheduleResponseFuture = stopCollect();
                    break;
                default:
                    throw new CollectorClientException(String.format("Invalid schedule request, action unknown: %s", action));
            }
            return scheduleResponseFuture.join();
        });
        LOG.debug("Immediately return from scheduleClient()");
        return scheduleResponseCF;
    }

    /**
     * Starts data collection.
     * <p>
     * Creates a {@code CollectorWorker} for every collector who performs the main data collection and the transport to external
     * data sinks.
     *
     * @param interval collect interval
     * @return a future with the {@code ScheduleResponse} of the schedule request.
     */
    private CompletableFuture<ScheduleResponse> startCollect(final Integer interval) {
        LOG.debug("Entering startCollect()");
        final CompletableFuture<ScheduleResponse> scheduleResponseCF = CompletableFuture.supplyAsync(() -> {
            if (!isRunning) {
                LOG.debug("Starting data collection, interval='{}'", interval);
                collectorTaskFutures = collectorRegistry.getCollectors()
                        .stream()
                        .map(collector -> new CollectorWorker(collector, clientPort, outboundWriter, instanceId))
                        .map(collectorWorker -> taskScheduler.scheduleWithFixedDelay(collectorWorker, interval))
                        .collect(Collectors.toList());
                isRunning = true;
                return new ScheduleResponse("Client started", isRunning, now(), registryAsStrings());
            }
            return new ScheduleResponse("Client already_running", isRunning, now(), registryAsStrings());
        });
        LOG.debug("Immediately return from startCollect()");
        return scheduleResponseCF;
    }

    /**
     * Stops data collection.
     * <p>
     *
     * @return a future with the {@code ScheduleResponse} of the schedule request.
     */
    private CompletableFuture<ScheduleResponse> stopCollect() {
        LOG.debug("Entering stopCollect()");
        final CompletableFuture<ScheduleResponse> scheduleResponseCF = CompletableFuture.supplyAsync(() -> {
            if (isRunning) {
                LOG.debug("Stopping data collection...");
                if (collectorTaskFutures != null && !collectorTaskFutures.isEmpty()) {
                    collectorTaskFutures.forEach(scheduledFuture -> scheduledFuture.cancel(false));
                }
                collectorTaskFutures = null;
                isRunning = false;
                LOG.debug("All collector tasks cancelled");
                return new ScheduleResponse("Client stopped", isRunning, now(), registryAsStrings());
            }
            return new ScheduleResponse("Client not running", isRunning, now(), registryAsStrings());
        });
        LOG.debug("Immediately return from stopCollect()");
        return scheduleResponseCF;
    }

    /**
     * Get metadata for the collector-client.
     *
     * @return a future with a map of metadata for the client.
     */
    public CompletableFuture<CollectorMetadata> getMetadata() {
        LOG.debug("Entering getMetadata()");
        final CompletableFuture<CollectorMetadata> metadataMapCF = CompletableFuture.supplyAsync(() -> {
            final CollectorMetadata metadata = new CollectorMetadata(instanceId, getHostName(),
                    registryAsStrings(), isRunning);
            LOG.debug("Fetched metadata: {}", metadata);
            return metadata;
        });
        LOG.debug("Immediately return from getMetadata()");
        return metadataMapCF;
    }

    /**
     * Get the host name of the client.
     *
     * @return a map containing host address an name
     */
    private static String getHostName() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            throw new CollectorClientException(e.getMessage());
        }
    }

    /**
     * Get registered {@code CollectorType}s as string list.
     *
     * @return registered clients as strings
     */
    private List<String> registryAsStrings() {
        return collectorRegistry.getCollectorTypes()
                .stream()
                .map(CollectorType::toString)
                .collect(Collectors.toList());
    }
}
