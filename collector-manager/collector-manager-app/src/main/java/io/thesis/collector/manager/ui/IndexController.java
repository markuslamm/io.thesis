package io.thesis.collector.manager.ui;

import com.google.common.collect.Maps;
import io.thesis.collector.manager.CollectorManagerException;
import io.thesis.collector.manager.discovery.CollectorClientInstanceService;
import io.thesis.collector.manager.ui.pages.ClientDetailsPage;
import io.thesis.collector.manager.ui.pages.IndexPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static java.time.LocalDateTime.now;
import static java.util.Objects.requireNonNull;

/**
 * spring-mvc controller that handles view requests for the {@code collector-manager}.
 */
@Controller
public class IndexController {

    private static final Logger LOG = LoggerFactory.getLogger(IndexController.class);

    private final CollectorClientInstanceService clientService;

    @Autowired
    public IndexController(final CollectorClientInstanceService clientService) {
        this.clientService = requireNonNull(clientService);
    }

    /**
     * Handles requests for  '/'.
     * <p>
     * Fetches all {@code collector-client} instances and adds the collection to the {@code Model}.
     *
     * @param model the model container
     * @return the name of the view to display
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public CompletableFuture<String> indexPage(final Model model) {
        LOG.debug("Entering indexPage()");
        final CompletableFuture<String> responseCP = clientService.getClientInstances()
                .thenApply(collectorClients -> {
                    LOG.debug("Fetched registered client instances: {}", collectorClients);
                    final Map<String, String> serverInfo = getHostInfo();
                    model.addAttribute("indexPage", new IndexPage(now(), serverInfo.get("serverHostName"),
                            serverInfo.get("serverHostAddress"), collectorClients));
                    return "index";

                });
        LOG.debug("Immediately return from indexPage()");
        return responseCP;
    }

    /**
     * Handles requests for client details.
     *
     * @param model the model container
     * @param host query parameter
     * @param port  query parameter
     * @return the name of the view to display
     */
    @RequestMapping(value = "/{host:.+}/{port}", method = RequestMethod.GET)
    public CompletableFuture<String> detailsPage(final Model model, @PathVariable final String host,
                                                 @PathVariable final Integer port) {
        LOG.debug("Entering detailsPage() for host={}, port={}", host, port);
        return clientService.getClientDetails(host, port)
                .thenApply(collectorClient -> {
                    LOG.debug("Fetched client for host '{}' and port '{}': {}", host, port, collectorClient);
                    final Map<String, String> serverInfo = getHostInfo();
                    model.addAttribute("clientDetailsPage", new ClientDetailsPage(now(), serverInfo.get("serverHostName"),
                            serverInfo.get("serverHostAddress"), collectorClient));
                    return "client-details";
                });
    }

    /**
     * Provides additional host information.
     *
     * @return a map containing host address an name
     */
    private static Map<String, String> getHostInfo() {
        try {
            final Map<String, String> result = Maps.newHashMap();
            result.put("serverHostAddress", InetAddress.getLocalHost().getHostAddress());
            result.put("serverHostName", InetAddress.getLocalHost().getHostName());
            return result;

        } catch (UnknownHostException e) {
            throw new CollectorManagerException(e.getMessage());
        }
    }
}
