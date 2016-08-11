package io.thesis.collector.manager.discovery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.discovery.DiscoveryClient;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

/**
 * Service component for fetching collector-client information from Consul service-discovery.
 */
public class CollectorClientInstanceService {

    private static final Logger LOG = LoggerFactory.getLogger(CollectorClientInstanceService.class);

    private final DiscoveryClient discoveryClient;
    private MetadataRestClient metadataRestClient;
    private final String clientAppName;

    public CollectorClientInstanceService(final DiscoveryClient discoveryClient, final MetadataRestClient metadataRestClient,
                                          @Value("${collector.client.discovery.app-name}") final String clientAppName) {
        this.discoveryClient = requireNonNull(discoveryClient);
        this.metadataRestClient = requireNonNull(metadataRestClient);
        this.clientAppName = requireNonNull(clientAppName);
    }

    /**
     * Fetches all {@code ServiceInstance}s for 'collector-client' instances from discovery-server.
     *
     * @return all 'collector-client' instances
     */
    public CompletableFuture<List<CollectorClientInstance>> getClientInstances() {
        LOG.debug("Entering getClientInstances()");
        final CompletableFuture<List<CollectorClientInstance>> instancesCP = CompletableFuture.supplyAsync(() -> {
            final List<CollectorClientInstance> clients = discoveryClient.getInstances(clientAppName)
                    .stream()
                    .map(serviceInstance ->
                            CollectorClientInstance.of(serviceInstance.getHost(), serviceInstance.getPort(),
                                    serviceInstance.getServiceId(), serviceInstance.isSecure(), serviceInstance.getUri()))
                    .collect(Collectors.toList());
            LOG.debug("Fetched all client instances: {}", clients);
            return clients;
        });
        LOG.debug("Immediately return from getClientInstances()");
        return instancesCP;
    }

    /**
     * Fetches detail information for a client instance.
     *
     * @param address filter parameter
     * @param port    filter parameter
     * @return client instance with detailed information
     */
    public CompletableFuture<CollectorClientInstance> getClientDetails(final String address, final int port) {
        LOG.debug("Entering getClientDetails({}, {})", address, port);
        final CompletableFuture<CollectorClientInstance> collectorClientInstanceCP = getClientInstances()
                .thenApply(collectorClientInstances ->
                        collectorClientInstances.stream()
                                .filter(serviceInstance -> serviceInstance.getAddress().equals(address))
                                .filter(serviceInstance -> serviceInstance.getPort() == port).findFirst()
                                .orElse(null)); //TODO
        final CompletableFuture<CollectorClientInstance> responseCP = metadataRestClient.getMetadata(address, port)
                .thenCombine(collectorClientInstanceCP, (metadataResult, clientInstance) ->
                        CollectorClientInstance.of(clientInstance.getAddress(), clientInstance.getPort(),
                                clientInstance.getServiceId(), clientInstance.getHttps(), clientInstance.getUri(),
                                metadataResult.getHostname(), metadataResult.getRegistry(),
                                metadataResult.getIsRunning(), metadataResult.getInstanceId()));
        LOG.debug("Immediately return from getClientDetails({}, {})", address, port);
        return responseCP;
    }
}
