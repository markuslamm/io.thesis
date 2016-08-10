package io.thesis.collector.client.ui.pages;

import com.google.common.base.MoreObjects;

import java.time.LocalDateTime;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

/**
 * Data container for collector-client index page.
 */
public final class IndexPage {

    private static final String UNKNOWN_KEY = "unknown";
    private final LocalDateTime serverTime;
    private final String hostName;
    private final String hostAddress;
    private final String sourceSystem;
    private final String instanceId;


    public IndexPage(final LocalDateTime serverTime, final String hostName, final String hostAddress, final String sourceSystem,
                     final String instanceId) {
        this.serverTime = requireNonNull(serverTime);
        this.hostName = requireNonNull(hostName);
        this.hostAddress = requireNonNull(hostAddress);
        this.sourceSystem = requireNonNull(sourceSystem);
        this.instanceId = requireNonNull(instanceId);
    }

    public LocalDateTime getServerTime() {
        return serverTime;
    }

    public String getHostName() {
        return Optional.ofNullable(hostName).orElse(UNKNOWN_KEY);
    }

    public String getHostAddress() {
        return Optional.ofNullable(hostAddress).orElse(UNKNOWN_KEY);
    }

    public String getSourceSystem() {
        return sourceSystem;
    }

    public String getInstanceId() {
        return instanceId;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).toString();
    }
}
