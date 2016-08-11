package io.thesis.collector.manager.discovery;

import org.springframework.http.HttpStatus;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;

/**
 * Wraps exceptions from external API calls.
 */
public class RestClientException extends RuntimeException {

    private static final long serialVersionUID = 5376770268823477587L;

    private final String url;

    public RestClientException(final String url, final HttpStatus status) {
        super(format("API call to '%s' resulted in HTTP status %s", url, status.toString()));
        this.url = requireNonNull(url);
    }

    public static RestClientException of(final String url, final HttpStatus status) {
        return new RestClientException(url, status);
    }
}
