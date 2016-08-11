package io.thesis.collector.commons;

import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

public abstract class AbstractCollector implements Collector {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractCollector.class);

    private final Map<String, SampleCollector> sampleRegistry;

    protected AbstractCollector(final Map<String, SampleCollector> sampleRegistry) {
        this.sampleRegistry = requireNonNull(sampleRegistry);
    }

    protected Map<String, SampleCollector> getSampleRegistry() {
        return sampleRegistry;
    }

    protected abstract void checkRegistry();

    protected abstract CollectorResult createResult(final Map<String, Object> dataMap);

    @Override
    public CompletableFuture<CollectorResult> collect() {
        LOG.debug("Entering AbstractCollector collect()");
        final CompletableFuture<CollectorResult> collectorResultCF = CompletableFuture.supplyAsync(() -> {
            checkRegistry();
            final List<CompletableFuture<Map<String, Object>>> sampleResultCPList = getSampleRegistry().values()
                    .stream()
                    .map(SampleCollector::collectSample)
                    .collect(Collectors.toList());

            return CompletableFuture.allOf(sampleResultCPList.toArray(new CompletableFuture[sampleResultCPList.size()]))
                    .thenApply(aVoid ->
                            sampleResultCPList
                                    .stream()
                                    .map(CompletableFuture::join)
                                    .collect(Collectors.toList()))
                    .thenApply(sampleResults -> {
                        final Map<String, Object> dataMap = Maps.newLinkedHashMap();
                        sampleResults.forEach(dataMap::putAll);
                        final CollectorResult collectorResult = createResult(dataMap);
                        LOG.debug("Finished AbstractCollector collect()");
                        return collectorResult;
                    }).join();
        });
        LOG.debug("Immediately return from AbstractCollector collect()");
        return collectorResultCF;
    }
}
