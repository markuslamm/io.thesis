package io.thesis.collector.manager.discovery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;

@Component
public class MetadataRestClient {

    private static final Logger LOG = LoggerFactory.getLogger(MetadataRestClient.class);

    private final RestTemplate restTemplate;
    private final String metadataPath;

    public MetadataRestClient(final RestTemplate restTemplate, final String metadataPath) {
        this.restTemplate = requireNonNull(restTemplate);
        this.metadataPath = requireNonNull(metadataPath);
    }

    public CompletableFuture<CollectorMetadataResult> getMetadata(final String clientHost, final Integer clientPort) {
        return CompletableFuture.supplyAsync(() -> {
            final URI fullUrl = getFullUrl(metadataPath, clientHost, clientPort);
            try {
                final ResponseEntity<CollectorMetadataResult> apiResponse =
                        restTemplate.exchange(fullUrl, HttpMethod.GET, HttpEntity.EMPTY, CollectorMetadataResult.class);
                if (!apiResponse.getStatusCode().is2xxSuccessful()) {
                    LOG.warn("API call failed, url={}, status={}", fullUrl, apiResponse.getStatusCode());
                    throw RestClientException.of(fullUrl.toString(), apiResponse.getStatusCode());
                }
                return Optional.ofNullable(apiResponse.getBody()).orElse(new CollectorMetadataResult());
            } catch (HttpClientErrorException | HttpServerErrorException ex) {
                LOG.warn("API call failed, url={}, status={}, exception={}, message={}", fullUrl, ex.getStatusCode(),
                        ex.getClass().getSimpleName(), ex.getMessage());
                throw RestClientException.of(fullUrl.toString(), ex.getStatusCode());
            }
        });
    }

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
}
