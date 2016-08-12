package io.thesis.collector.commons.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;

/**
 * Base class for all clients, who need to perform REST calls to an external API based on
 * Springs {@code RestTemplate}.
 * <p>
 * Subclasses provide necessary dependencies and host/port configuration via
 * dependency injection.
 */
public abstract class AbstractRestClient {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractRestClient.class);

    private final RestTemplate restTemplate;
    private final String host;
    private final Integer port;

    protected AbstractRestClient(final RestTemplate restTemplate, final String host, final Integer port) {
        this.restTemplate = requireNonNull(restTemplate);
        this.host = requireNonNull(host);
        this.port = requireNonNull(port);
    }

    /**
     * The main REST call via {@code RestTemplate}. Will be called by overloaded executeGets.
     *
     * @param uri          the uri of the API call
     * @param responseType the expected JSON result object
     * @param <T>          class type of the result object
     * @return a typed {@code ResponseEntity} containing the JSON result and {@code @HttpStatus}
     */
    private <T> ResponseEntity<T> executeGet(final URI uri, final Class<T> responseType) {
        final ResponseEntity<T> apiResponse;
        try {
            apiResponse = restTemplate().exchange(getFullUrl(uri), HttpMethod.GET, emptyRequestEntity(), responseType);
            if (!apiResponse.getStatusCode().is2xxSuccessful()) {
                LOG.warn("API call failed, url={}, status={}", uri.toString(), apiResponse.getStatusCode());
                throw RestClientException.of(uri.toString(), apiResponse.getStatusCode());
            }
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            LOG.warn("API call failed, url={}, status={}, exception={}, message={}", uri.toString(), ex.getStatusCode(),
                    ex.getClass().getSimpleName(), ex.getMessage());
            throw RestClientException.of(uri.toString(), ex.getStatusCode());
        }
        return apiResponse;
    }

    /**
     * Creates the full url with host and port information.
     *
     * @param apiPath the constructed API path of the url
     * @return the full url the {@code RestTemplate} takes for the request
     */
    private URI getFullUrl(final URI apiPath) {
        return UriComponentsBuilder
                .fromHttpUrl(format("http://%s:%d%s", host, port, apiPath.toString()))
                .build().toUri();
    }

    /**
     * Constructs an {@code URI} based on the API path.
     *
     * @param apiPath API path
     * @return URI with API path
     */
    private URI buildUri(final String apiPath) {
        return UriComponentsBuilder.fromPath(apiPath).build(false).toUri();
    }

    /**
     * Constructs an {@code URI} based on the API path and a {@code PathVariable}.
     *
     * @param apiPath      the API path
     * @param pathVariable the path variable
     * @return uri with API path and expanded {@code PathVariable}
     */
    private URI buildUriWithPathVariable(final String apiPath, final String pathVariable) {
        final UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromPath(apiPath);
        return uriBuilder.buildAndExpand(pathVariable).toUri();
    }

    private RestTemplate restTemplate() {
        return restTemplate;
    }

    private HttpEntity<?> emptyRequestEntity() {
        return new HttpEntity<>(new HttpHeaders());
    }

    /**
     * Executes a REST call based on the API path and the response class type.
     *
     * @param apiPath      the API path
     * @param responseType class type of the response body object
     * @param <T>          class type of the response body object
     * @return a typed {@code ResponseEntity} containing the JSON response body and {@code HttpStatus}
     */
    protected <T> ResponseEntity<T> executeGet(final String apiPath, final Class<T> responseType) {
        final URI uri = buildUri(apiPath);
        return executeGet(uri, responseType);
    }

    /**
     * Executes a REST call based on the API path, the response class type and a {@code PathVariable}.
     *
     * @param apiPath      the API path
     * @param responseType class type of the response body object
     * @param pathVariable the path variable
     * @param <T>          class type of the response body object
     * @return a typed {@code ResponseEntity} containing the JSON result and {@code HttpStatus}
     */
    protected <T> ResponseEntity<T> executeGet(final String apiPath, final Class<T> responseType,
                                               final String pathVariable) {
        final URI uri = buildUriWithPathVariable(apiPath, pathVariable);
        return executeGet(uri, responseType);
    }
}
