package io.thesis.commons.rest;

import org.springframework.http.HttpStatus;

import static java.util.Objects.requireNonNull;

/**
 * Will be thrown if API call resulted in an status different from 2xx or in case of client/server exceptions.
 */
public class RestClientException extends RuntimeException {

    private static final long serialVersionUID = 5376770268823477587L;

    private final String url;

    public RestClientException(final String url, final HttpStatus status) {
        super(String.format("API call to '%s' resulted in HTTP status %s", url, status.toString()));
        this.url = requireNonNull(url);
    }

    public static RestClientException of(final String url, final HttpStatus status) {
        return new RestClientException(url, status);
    }

    public String getUrl() {
        return url;
    }
}
