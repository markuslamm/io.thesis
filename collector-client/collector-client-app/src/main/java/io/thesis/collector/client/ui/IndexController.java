package io.thesis.collector.client.ui;

import com.google.common.collect.Maps;
import io.thesis.collector.client.CollectorClientException;
import io.thesis.collector.client.ui.pages.IndexPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import static java.time.LocalDateTime.now;

/**
 * Handles view requests for the {@code collector-client}.
 */
@Controller
public class IndexController {

    private static final Logger LOG = LoggerFactory.getLogger(IndexController.class);

    private final String sourceSystem;
    private final String instanceId;

    public IndexController(@Value("${info.system}") final String sourceSystem,
                           @Value("${spring.cloud.consul.discovery.instanceId}") final String instanceId) {
        this.sourceSystem = Objects.requireNonNull(sourceSystem);
        this.instanceId = Objects.requireNonNull(instanceId);
    }

    /**
     * Handles requests for  '/'.
     *
     * @param model the model container
     * @return the name of the view to display
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public CompletableFuture<String> indexPage(final Model model) {
        LOG.debug("Entering indexPage()");
        final CompletableFuture<String> responseCP = CompletableFuture.supplyAsync(() -> {
            final Map<String, String> serverInfo = getHostInfo();
            model.addAttribute("indexPage", new IndexPage(now(), serverInfo.get("serverHostName"),
                    serverInfo.get("serverHostAddress"), sourceSystem, instanceId));
            return "index";
        });
        LOG.debug("Immediately return from indexPage()");
        return responseCP;
    }

    /**
     * Provides additional host information of the collector-client.
     *
     * @return a map containing host address and name
     */
    private static Map<String, String> getHostInfo() {
        try {
            final Map<String, String> result = Maps.newHashMap();
            result.put("serverHostAddress", InetAddress.getLocalHost().getHostAddress());
            result.put("serverHostName", InetAddress.getLocalHost().getHostName());
            return result;
        } catch (UnknownHostException e) {
            throw new CollectorClientException(e.getMessage());
        }
    }
}
