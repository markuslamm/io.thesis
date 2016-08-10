package io.thesis.collector.manager.ui.pages;

import java.time.LocalDateTime;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

/**
 * Base class for all collector-manager pages that holds common data.
 */
public abstract class AbstractManagerPage {

    protected static final String UNKNOWN_KEY = "unknown";

    private final LocalDateTime serverTime;
    private final String serverHostName;
    private final String serverAddress;

    protected AbstractManagerPage(final LocalDateTime serverTime, final String serverHostName, final String serverAddress) {
        this.serverTime = requireNonNull(serverTime);
        this.serverHostName = requireNonNull(serverHostName);
        this.serverAddress = requireNonNull(serverAddress);
    }

    public LocalDateTime getServerTime() {
        return serverTime;
    }

    public String getServerHostName() {
        return Optional.ofNullable(serverHostName).orElse(UNKNOWN_KEY);
    }

    public String getServerAddress() {
        return Optional.ofNullable(serverAddress).orElse(UNKNOWN_KEY);
    }
}
