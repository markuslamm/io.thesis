package io.thesis.collector.manager.discovery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

/**
 * Service component for fetching collector-client information from Consul service-discovery.
 */
@Component
public class CollectorClientInstanceService {

    private static final Logger LOG = LoggerFactory.getLogger(CollectorClientInstanceService.class);

    private final DiscoveryClient discoveryClient;
    private final RestTemplate restTemplate;
    private final String clientAppName;

    @Autowired
    public CollectorClientInstanceService(final DiscoveryClient discoveryClient, final RestTemplate restTemplate,
                                          @Value("${collector.client.discovery.app-name}") final String clientAppName) {
        this.discoveryClient = requireNonNull(discoveryClient);
        this.restTemplate = requireNonNull(restTemplate);
        this.clientAppName = requireNonNull(clientAppName);
    }

    /**
     * Fetches all {@code ServiceInstance}s for 'collector-client' instances from discovery-server.
     *
     * @return all 'collector-client' instances
     */
    public CompletableFuture<List<CollectorClientInstance>> getClientInstances() {
        return CompletableFuture.supplyAsync(() -> {
            final List<CollectorClientInstance> clients = discoveryClient.getInstances(clientAppName)
                    .stream()
                    .map(serviceInstance ->
                            CollectorClientInstance.of(serviceInstance.getHost(), serviceInstance.getPort(),
                                    serviceInstance.getServiceId(), serviceInstance.isSecure(), serviceInstance.getUri()))
                    .collect(Collectors.toList());
            LOG.debug("Fetched all client instances: {}", clients);
            return clients;
        });
    }

//    /**
//     * Fetches detail information of a client instance.
//     *
//     * @param address filter parameter
//     * @param port    filter parameter
//     * @return client instance with detailed information
//     */
//    public CompletableFuture<CollectorClientInstance> getClientDetails(final String address, final int port) {
//        final CompletableFuture<CollectorClientInstance> collectorClientInstanceCP = getClientInstances()
//                .thenApply(collectorClientInstances ->
//                        collectorClientInstances.stream()
//                                .filter(serviceInstance -> serviceInstance.getAddress().equals(address))
//                                .filter(serviceInstance -> serviceInstance.getPort() == port).findFirst()
//                                .orElse(null)); //TODO
//        return getCollectorMetadataFuture(address, port)
//                .thenCombine(collectorClientInstanceCP, (metadataResult, clientInstance) ->
//                        CollectorClientInstance.of(clientInstance.getAddress(), clientInstance.getPort(),
//                                clientInstance.getServiceId(), clientInstance.getHttps(), clientInstance.getUri(),
//                                metadataResult.getSystem(), metadataResult.getHostname(),
//                                metadataResult.getCollectorRegistry(), metadataResult.getIsRunning(),
//                                metadataResult.getInstanceId()));
//    }
//    }
}
