package io.thesis.collector.client.schedule;

import io.thesis.collector.client.CollectorClient;
import io.thesis.collector.client.CollectorClientException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.concurrent.CompletableFuture;

import static java.util.Objects.requireNonNull;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * spring-mvc controller that provides an endpoint for scheduling client's data collection.
 * <p>
 * Used by collector-server start/stop the collection process of a client.
 */
@RestController
@RequestMapping(produces = APPLICATION_JSON_VALUE)
public class ScheduleController {

    private static final Logger LOG = LoggerFactory.getLogger(ScheduleController.class);

    private final CollectorClient collectorClient;

    @Autowired
    public ScheduleController(final CollectorClient collectorClient) {
        this.collectorClient = requireNonNull(collectorClient);
    }

    /**
     * An endpoint for starting/stopping the collection process.
     *
     * @return a future with the {@code ScheduleResponse} of the schedule request wrapped in a {@code ResponseEntity} that
     * contains additional HTTP status data.
     *
     * @param request schedule request data
     * @param errors possible request errors
     */
    @CrossOrigin(origins = "http://localhost:9090")
    @RequestMapping(value = "/client/schedule", method = RequestMethod.POST, consumes = APPLICATION_JSON_VALUE)
    public CompletableFuture<ResponseEntity<ScheduleResponse>> schedule(@RequestBody @Valid final ScheduleRequest request,
                                                                        final BindingResult errors) {
        LOG.debug("Received schedule request, action={}, interval={}", request.getAction(), request.getInterval());
        if (errors.hasErrors()) {
            throw new CollectorClientException("Invalid schedule request");
        }
        return collectorClient.scheduleClient(request)
                .thenApply(collectorScheduleResponse ->
                        new ResponseEntity<>(collectorScheduleResponse, HttpStatus.OK));
    }
}
