package io.thesis.collector.manager.discovery;

import com.google.common.collect.Lists;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.net.URI;
import java.util.List;

/**
 * Domain class representing a registered collector-client, based on {@code ServiceInstance} data,
 * obtained from Consul service-discovery and metadata requested from clients '/metadata' endpoint.
 */
public final class CollectorClientInstance {

    private final String address;
    private final Integer port;
    private final String serviceId;
    private final Boolean https;
    private final URI uri;
    private final String system;
    private final String hostname;
    private final List<String> collectorRegistry;
    private final Boolean isRunning;
    private final String instanceId;

    private CollectorClientInstance(final String address, final Integer port, final String serviceId,
                                    final Boolean https, final URI uri, final String system, final String hostname,
                                    final List<String> collectorRegistry, final Boolean isRunning, final String instanceId) {
        this.address = address;
        this.port = port;
        this.serviceId = serviceId;
        this.https = https;
        this.uri = uri;
        this.system = system;
        this.hostname = hostname;
        this.collectorRegistry = collectorRegistry;
        this.isRunning = isRunning;
        this.instanceId = instanceId;
    }

    public static CollectorClientInstance of(final String address, final Integer port, final String serviceId,
                                             final Boolean https, final URI uri, final String system, final String hostname,
                                             final List<String> collectorRegistry, final Boolean isRunning,
                                             final String instanceId) {
        return new CollectorClientInstance(address, port, serviceId, https, uri, system, hostname, collectorRegistry, isRunning,
                instanceId);
    }

    public static CollectorClientInstance of(final String address, final Integer port, final String serviceId,
                                             final Boolean https, final URI uri) {
        return new CollectorClientInstance(address, port, serviceId, https, uri, null, null, Lists.newArrayList(), null, null);
    }

    public String getAddress() {
        return address;
    }

    public Integer getPort() {
        return port;
    }

    public String getServiceId() {
        return serviceId;
    }

    public Boolean getHttps() {
        return https;
    }

    public URI getUri() {
        return uri;
    }

    public String getSystem() {
        return system;
    }

    public String getHostname() {
        return hostname;
    }

    public List<String> getCollectorRegistry() {
        return collectorRegistry;
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
