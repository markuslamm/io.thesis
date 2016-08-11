package io.thesis.collector.client;

import io.thesis.collector.commons.CollectorType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

/**
 * The main component for collecting data from source systems the client is installed on.
 * It manages registered collectors in a {@code CollectorRegistry} and the scheduling of user-triggered
 * data collection.
 */
public class CollectorClient {

    private static final Logger LOG = LoggerFactory.getLogger(CollectorClient.class);

    private final CollectorRegistry collectorRegistry;
    private final String instanceId;
    private final Integer clientPort;

    private volatile boolean isRunning = false;

    public CollectorClient(final CollectorRegistry collectorRegistry, final String instanceId,final Integer clientPort) {
        this.collectorRegistry = requireNonNull(collectorRegistry);
        this.instanceId = requireNonNull(instanceId);
        this.clientPort = requireNonNull(clientPort);
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
