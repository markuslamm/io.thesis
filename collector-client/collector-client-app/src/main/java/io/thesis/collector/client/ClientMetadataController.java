package io.thesis.collector.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

import static java.util.Objects.requireNonNull;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * Provides an endpoint for fetching metadata of the collector-client.
 * <p>
 * Used by collector-manager to display metadata in the server UI.
 */
@RestController
@RequestMapping(value = "/client/metadata", produces = APPLICATION_JSON_VALUE)
public class ClientMetadataController {

    private static final Logger LOG = LoggerFactory.getLogger(ClientMetadataController.class);

    private final CollectorClient collectorClient;

    @Autowired
    public ClientMetadataController(final CollectorClient collectorClient) {
        this.collectorClient = requireNonNull(collectorClient);
    }

    /**
     * An endpoint for retrieving metadata from the {@code CollectorClient}.
     *
     * @return a future with a map of metadata for the client.
     */
    @Async
    @RequestMapping(method = RequestMethod.GET)
    public CompletableFuture<ResponseEntity<CollectorMetadata>> getMetadata() {
        LOG.debug("Entering getMetadata()");
        final CompletableFuture<ResponseEntity<CollectorMetadata>> responseCP = collectorClient.getMetadata()
                .thenApply(metadataMap -> {
                    LOG.debug("Fetched client metadata: {}", metadataMap);
                    return new ResponseEntity<>(metadataMap, HttpStatus.OK);
                });
        LOG.debug("Immediately return from getMetadata()");
        return responseCP;
    }
}
