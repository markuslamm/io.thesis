package io.thesis.collector.manager.discovery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;

/**
 * Service component for fetching collector-client information from Consul service-discovery.
 */
@Component
public class CollectorClientInstanceService {

    private static final Logger LOG = LoggerFactory.getLogger(CollectorClientInstanceService.class);

    private final DiscoveryClient discoveryClient;
    private final RestTemplate restTemplate;
    private final String metadataPath;
    private final String clientAppName;

    @Autowired
    public CollectorClientInstanceService(final DiscoveryClient discoveryClient, final RestTemplate restTemplate,
                                          @Value("${collector.client.metadata.path}") final String metadataPath,
                                          @Value("${collector.client.discovery.app-name}") final String clientAppName) {
        this.discoveryClient = requireNonNull(discoveryClient);
        this.restTemplate = requireNonNull(restTemplate);
        this.metadataPath = requireNonNull(metadataPath);
        this.clientAppName = requireNonNull(clientAppName);
    }

    /**
     * Fetches all {@code ServiceInstance}s for 'collector-client' instances from discovery-server.
     *
     * @return all 'collector-client' instances
     */
    public CompletableFuture<List<CollectorClientInstance>> getClientInstances() {
        return CompletableFuture.supplyAsync(() -> {
            final List<CollectorClientInstance> clients = discoveryClient.getInstances("collector-server")
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
//
//    /**
//     * Fetches metadata for client instance from the /client/metadata endpoint. Contains data that is
//     * not available via {@code ServiceInstance}s form collector-client-discovery.
//     *
//     * @param host ip of the client instance
//     * @param port port of the client instance
//     * @return metadata result
//     */
//    private CompletableFuture<CollectorMetadataResult> getCollectorMetadataFuture(final String host, final int port) {
//        return CompletableFuture.supplyAsync(() -> {
//            final URI fullUrl = getFullUrl(COLLECTOR_CLIENT_METADATA_PATH, host, port);
//            try {
//                final ResponseEntity<CollectorMetadataResult> apiResponse =
//                        restTemplate.exchange(fullUrl, HttpMethod.GET, emptyRequestEntity(), CollectorMetadataResult.class);
//                if (!apiResponse.getStatusCode().is2xxSuccessful()) {
//                    LOG.warn("API call failed, url={}, status={}", fullUrl, apiResponse.getStatusCode());
//                    throw RestClientException.of(fullUrl.toString(), apiResponse.getStatusCode());
//                }
//                return Optional.ofNullable(apiResponse.getBody()).orElse(null);
//            } catch (HttpClientErrorException | HttpServerErrorException ex) {
//                LOG.warn("API call failed, url={}, status={}, exception={}, message={}", fullUrl, ex.getStatusCode(),
//                        ex.getClass().getSimpleName(), ex.getMessage());
//                throw RestClientException.of(fullUrl.toString(), ex.getStatusCode());
//            }
//        });
//    }

    /**
     * Constructs the full url for the collector-client metadata API request.
     *
     * @param apiPath API path
     * @param host    ip of the client instance
     * @param port    port of the client instance
     * @return the full uri
     */
    private static URI getFullUrl(final String apiPath, final String host, final int port) {
        return UriComponentsBuilder
                .fromHttpUrl(format("http://%s:%d%s", host, port, apiPath))
                .build().toUri();
    }

    /**
     * Creates empty request entity, needed by {@code RestTemplate}
     *
     * @return empty HTTP entity
     */
    private HttpEntity<?> emptyRequestEntity() {
        return new HttpEntity<>(new HttpHeaders());
    }

}
