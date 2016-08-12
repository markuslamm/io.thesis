package io.thesis.collector.dstat;

import com.google.common.collect.Maps;
import io.thesis.collector.commons.Collector;
import io.thesis.collector.commons.CollectorResult;
import io.thesis.collector.commons.CollectorType;
import io.thesis.collector.dstat.samples.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static java.lang.String.format;

/**
 * Collects data from dstat system utility. Uses an internal registry of {@code AbstractDstatSampleCollector}s
 * and aggregates their results.
 */
public final class DstatCollector implements Collector {

    private static final Logger LOG = LoggerFactory.getLogger(DstatCollector.class);

    private static final String[] DSTAT_COMMAND = {"dstat", "-t",
            "--cpu", "--top-cpu-adv", "--top-cputime", "--top-cputime-avg",
            "--disk", "--disk-tps", "--disk-util",
            "--net", "--socket", "--tcp", "--udp",
            "--io", "--top-io-adv", "--lock", "--fs",
            "--mem", "--top-mem", "--page", "--swap", "--vm",
            "--sys", "--load", "--ipc", "--unix",
            "--proc", "--proc-count", "--top-latency", "--top-latency-avg",
            "--full",
            "--float", "1", "0"};

    private Map<String, AbstractDstatSampleCollector> sampleRegistry;

    public DstatCollector() {
        this.sampleRegistry = defaultSampleRegistry();
    }

    @Override
    public CompletableFuture<CollectorResult> collect() {
        LOG.debug("Entering {} collect()", CollectorType.DSTAT);
        final CompletableFuture<CollectorResult> collectorResultCF = CompletableFuture.supplyAsync(() -> {
            if (sampleRegistry.isEmpty()) {
                throw new DStatCollectorException(format("No registered %s collectors", CollectorType.DSTAT));
            }
            try {
                final ProcessBuilder processBuilder = new ProcessBuilder(DSTAT_COMMAND);
                processBuilder.redirectErrorStream(true);
                final Process process = processBuilder.start();
                try (BufferedReader processOutputReader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                    final String dstatResult = processOutputReader.lines()
                            .map(String::toString)
                            .collect(Collectors.joining(System.lineSeparator()));
                    final int exitCode = process.waitFor();
                    LOG.debug("Dstat exit code: {}", exitCode);
                    LOG.debug("Dstat output: {}", dstatResult); //
                    final List<CompletableFuture<Map<String, Object>>> dStatDataFuturesList =
                            sampleRegistry.values().stream()
                                    .map(collector ->
                                            CompletableFuture.supplyAsync(() -> collector.apply(dstatResult)))
                                    .collect(Collectors.toList());

                    return CompletableFuture.allOf(dStatDataFuturesList.toArray(new CompletableFuture[dStatDataFuturesList.size()]))
                            .thenApply(aVoid ->
                                    dStatDataFuturesList
                                            .stream()
                                            .map(CompletableFuture::join)
                                            .collect(Collectors.toList()))
                            .thenApply(dstatSamples -> {
                                final Map<String, Object> data = Maps.newLinkedHashMap();
                                dstatSamples.forEach(data::putAll);
                                final CollectorResult dstatCollectorResult =
                                        new CollectorResult(CollectorType.DSTAT.name().toLowerCase(), data);
                                LOG.debug("Finished {} collecting", CollectorType.DSTAT);
                                return dstatCollectorResult;
                            }).join();
                }
            } catch (IOException | InterruptedException ex) {
                throw new DStatCollectorException(format("Unable to collect %s data: %s", ex.getMessage(), CollectorType.DSTAT.name()));
            }
        });
        LOG.debug("Immediately return from {} collect()", CollectorType.DSTAT);
        return collectorResultCF;
    }

    @Override
    public CollectorType getCollectorType() {
        return CollectorType.DSTAT;
    }

    private static Map<String, AbstractDstatSampleCollector> defaultSampleRegistry() {
        final Map<String, AbstractDstatSampleCollector> registry = Maps.newHashMap();
        registry.put(CpuSampleCollector.SAMPLE_KEY, new CpuSampleCollector());
        registry.put(DiskSampleCollector.SAMPLE_KEY, new DiskSampleCollector());
        registry.put(IoSampleCollector.SAMPLE_KEY, new IoSampleCollector());
        registry.put(MemorySampleCollector.SAMPLE_KEY, new MemorySampleCollector());
        registry.put(NetSampleCollector.SAMPLE_KEY, new NetSampleCollector());
        registry.put(ProcessSampleCollector.SAMPLE_KEY, new ProcessSampleCollector());
        registry.put(SystemSampleCollector.SAMPLE_KEY, new SystemSampleCollector());
        return registry;
    }
}
