package io.thesis.collector.commons.rest;

import io.thesis.collector.commons.CollectorException;
import org.springframework.http.HttpStatus;

import static java.util.Objects.requireNonNull;

/**
 * Will be thrown if API call resulted in an status different from 2xx or in case of client/server exceptions.
 */
public class RestCollectorException extends CollectorException {

    private static final long serialVersionUID = 5376770268823477587L;

    private final String url;

    public RestCollectorException(final String url, final HttpStatus status) {
        super(String.format("API call to '%s' resulted in HTTP status %s", url, status.toString()));
        this.url = requireNonNull(url);
    }

    public static RestCollectorException of(final String url, final HttpStatus status) {
        return new RestCollectorException(url, status);
    }

    public String getUrl() {
        return url;
    }
}
