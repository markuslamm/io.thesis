package io.thesis.collector.manager.ui.pages;//package io.thesis.collector.server.ui.pages;

import io.thesis.collector.manager.discovery.CollectorClientInstance;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.Objects.requireNonNull;

/**
 * Data container for collector-server client details page.
 */
public final class ClientDetailsPage extends AbstractManagerPage {

    private final String hostname;
    private final String address;
    private final Integer port;
    private final String url;
    private final Boolean https;
    private final List<String> registry;
    private final Boolean isRunning;
    private final String instanceId;

    public ClientDetailsPage(final LocalDateTime serverTime, final String serverHostName, final String serverAddress,
                             final CollectorClientInstance clientInstance) {
        super(serverTime, serverHostName, serverAddress);
        final CollectorClientInstance instance = requireNonNull(clientInstance);
        this.hostname = instance.getHostname();
        this.address = instance.getAddress();
        this.port = instance.getPort();
        this.https = instance.getHttps();
        this.url = instance.getUri().toString();
        this.registry = instance.getCollectorRegistry();
        this.isRunning = instance.getIsRunning();
        this.instanceId = instance.getInstanceId();
    }

    public String getHostname() {
        return hostname;
    }

    public String getAddress() {
        return address;
    }

    public Integer getPort() {
        return port;
    }

    public String getUrl() {
        return url;
    }

    public Boolean getHttps() {
        return https;
    }

    public List<String> getRegistry() {
        return registry;
    }

    public Boolean getIsRunning() {
        return isRunning;
    }

    public String getInstanceId() {
        return instanceId;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
