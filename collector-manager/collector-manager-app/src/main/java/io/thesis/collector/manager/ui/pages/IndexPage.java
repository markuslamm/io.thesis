package io.thesis.collector.manager.ui.pages;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.time.LocalDateTime;

/**
 * Holds data for collector-manager index page.
 */
public final class IndexPage extends AbstractManagerPage {

    public IndexPage(final LocalDateTime serverTime, final String serverHostName, final String serverAddress) {
        super(serverTime, serverHostName, serverAddress);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
