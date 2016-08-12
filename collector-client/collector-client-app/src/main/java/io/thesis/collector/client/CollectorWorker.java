package io.thesis.collector.client;

import io.thesis.collector.client.outbound.OutboundWriter;
import io.thesis.collector.commons.Collector;
import io.thesis.collector.commons.CollectorResult;
import io.thesis.commons.json.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;

import static java.time.LocalDateTime.now;
import static java.util.Objects.requireNonNull;

/**
 * The "worker" component of the collector-client.
 *
 * Collects data by delegating to an underlying {@code Collector} implementation and sends results to an
 * implementation of {@code OutboundWriter}.
 */
class CollectorWorker implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(CollectorWorker.class);

    private final Collector collector;
    private final Integer clientPort;
    private final OutboundWriter outboundWriter;
    private final String instanceId;

    public CollectorWorker(final Collector collector, final Integer clientPort,
                           final OutboundWriter outboundWriter, final String instanceId) {
        this.collector = requireNonNull(collector);
        this.clientPort = requireNonNull(clientPort);
        this.outboundWriter = requireNonNull(outboundWriter);
        this.instanceId = requireNonNull(instanceId);
    }

    /**
     * Collects the data of the {@code Collector}, creates a JSON representation and writes the result.
     */
    @Override
    public void run() {
        LOG.debug("Collector worker starts...");
        collector.collect()
                .thenAccept(collectorResult -> {
                    try {
                        final CollectorResult result = new CollectorResult(collectorResult.getCollectorType(),
                                collectorResult.getData(), now(), InetAddress.getLocalHost().getHostAddress(),
                                clientPort, instanceId);
                        final String jsonData = JsonUtils.toJson(result);
                        System.err.println(jsonData);
                        outboundWriter.write(jsonData);
                    } catch (UnknownHostException ex) {
                        throw new CollectorClientException(ex.getMessage());
                    }
                });
        LOG.debug("Collector worker finished");
    }
}
