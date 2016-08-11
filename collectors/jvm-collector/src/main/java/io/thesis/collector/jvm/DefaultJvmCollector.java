//package io.thesis.collector.jvm;
//
//import com.google.common.collect.Maps;
//import io.thesis.collector.commons.Collector;
//import io.thesis.collector.commons.CollectorResult;
//import io.thesis.collector.commons.CollectorType;
//import io.thesis.collector.commons.SampleCollector;
//import io.thesis.collector.commons.jmx.JmxCollectorException;
//import io.thesis.collector.jvm.samples.*;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import javax.management.MBeanServerConnection;
//import java.util.List;
//import java.util.Map;
//import java.util.concurrent.CompletableFuture;
//import java.util.stream.Collectors;
//
//import static java.lang.String.format;
//
///**
// * Collects default JVM data via the JMX interface. Uses an internal registry of {@code SampleCollector}s
// * and aggregates their results.
// */
//public final class DefaultJvmCollector implements Collector {
//
//    private static final Logger LOG = LoggerFactory.getLogger(DefaultJvmCollector.class);
//
//    private Map<String, SampleCollector> sampleRegistry;
//
//    public DefaultJvmCollector(final MBeanServerConnection mBeanServerConnection) {
//        this.sampleRegistry = defaultSampleRegistry(mBeanServerConnection);
//    }
//
//    /**
//     * Collect default JVM data by aggregating the results of all registered {@code SampleCollector}s.
//     *
//     * @return a future with the result of a collection process.
//     */
//    @Override
//    public CompletableFuture<CollectorResult> collect() {
//        LOG.debug("Entering {} collect()", CollectorType.DEFAULT_JVM_JMX);
//        final CompletableFuture<CollectorResult> collectorResultCF = CompletableFuture.supplyAsync(() -> {
//            if (sampleRegistry.isEmpty()) {
//                throw new JmxCollectorException(format("No registered %s collectors", CollectorType.DEFAULT_JVM_JMX));
//            }
//            final List<CompletableFuture<Map<String, Object>>> mBeanSamplesFutures = sampleRegistry.values()
//                    .stream()
//                    .map(SampleCollector::collectSample)
//                    .collect(Collectors.toList());
//
//            return CompletableFuture.allOf(mBeanSamplesFutures.toArray(new CompletableFuture[mBeanSamplesFutures.size()]))
//                    .thenApply(aVoid ->
//                            mBeanSamplesFutures
//                                    .stream()
//                                    .map(CompletableFuture::join)
//                                    .collect(Collectors.toList()))
//                    .thenApply(mBeanSamples -> {
//                        final Map<String, Object> data = Maps.newLinkedHashMap();
//                        mBeanSamples.forEach(data::putAll);
//                        final CollectorResult jmxCollectorResult =
//                                new CollectorResult(CollectorType.DEFAULT_JVM_JMX.name().toLowerCase(), data);
//                        LOG.debug("Finished {} collecting", CollectorType.DEFAULT_JVM_JMX);
//                        return jmxCollectorResult;
//                    }).join();
//        });
//        LOG.debug("Immediately return from {} collect()", CollectorType.DEFAULT_JVM_JMX);
//        return collectorResultCF;
//    }
//
//    @Override
//    public CollectorType getCollectorType() {
//        return CollectorType.DEFAULT_JVM_JMX;
//    }
//
//    private static Map<String, SampleCollector> defaultSampleRegistry(final MBeanServerConnection mBeanServerConnection) {
//        final Map<String, SampleCollector> registry = Maps.newHashMap();
//        registry.put(ClassloadingSampleCollector.SAMPLE_KEY, new ClassloadingSampleCollector(mBeanServerConnection));
//        registry.put(BufferPoolSampleCollector.SAMPLE_KEY, new BufferPoolSampleCollector(mBeanServerConnection));
//        registry.put(GcSampleCollector.SAMPLE_KEY, new GcSampleCollector(mBeanServerConnection));
//        registry.put(MemorySampleCollector.SAMPLE_KEY, new MemorySampleCollector(mBeanServerConnection));
//        registry.put(MemoryPoolSampleCollector.SAMPLE_KEY, new MemoryPoolSampleCollector(mBeanServerConnection));
//        registry.put(OsSampleCollector.SAMPLE_KEY, new OsSampleCollector(mBeanServerConnection));
//        registry.put(RuntimeSampleCollector.SAMPLE_KEY, new RuntimeSampleCollector(mBeanServerConnection));
//        registry.put(ThreadSampleCollector.SAMPLE_KEY, new ThreadSampleCollector(mBeanServerConnection));
//        return registry;
//    }
//}
