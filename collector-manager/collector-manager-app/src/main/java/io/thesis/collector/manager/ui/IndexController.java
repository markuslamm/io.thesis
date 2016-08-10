package io.thesis.collector.manager.ui;

import com.google.common.collect.Maps;
import io.thesis.collector.manager.CollectorManagerException;
import io.thesis.collector.manager.ui.pages.IndexPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static java.time.LocalDateTime.now;

/**
 * spring-mvc controller that handles view requests for {@code collector-server}.
 */
@Controller
public class IndexController {

    private static final Logger LOG = LoggerFactory.getLogger(IndexController.class);

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
        return CompletableFuture.supplyAsync(() -> {
            final Map<String, String> serverInfo = getServerInfo();
            model.addAttribute("indexPage", new IndexPage(now(), serverInfo.get("serverHostName"),
                    serverInfo.get("serverHostAddress")));
            return "index";
        });
    }

    /**
     * Provides additional host information.
     *
     * @return a map containing host address an name
     */
    private static Map<String, String> getServerInfo() {
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
