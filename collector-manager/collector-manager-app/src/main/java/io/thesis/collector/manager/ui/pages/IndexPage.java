package io.thesis.collector.manager.ui.pages;

import io.thesis.collector.manager.discovery.CollectorClientInstance;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Objects;

/**
 * Holds data for collector-manager index page.
 */
public final class IndexPage extends AbstractManagerPage {

    private final Collection<CollectorClientInstance> clientInstanceList;

    public IndexPage(final LocalDateTime serverTime, final String serverHostName, final String serverAddress,
                     final Collection<CollectorClientInstance> clientInstanceList) {
        super(serverTime, serverHostName, serverAddress);
        this.clientInstanceList = Objects.requireNonNull(clientInstanceList);
    }

    public Collection<CollectorClientInstance> getClientInstanceList() {
        return clientInstanceList;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
