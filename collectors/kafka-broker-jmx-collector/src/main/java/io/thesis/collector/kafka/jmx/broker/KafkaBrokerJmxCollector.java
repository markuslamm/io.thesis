package io.thesis.collector.kafka.jmx.broker;

import com.google.common.collect.Maps;
import io.thesis.collector.commons.AbstractCollector;
import io.thesis.collector.commons.CollectorType;
import io.thesis.collector.commons.SampleCollector;
import io.thesis.collector.commons.jmx.JmxCollectorException;
import io.thesis.collector.kafka.jmx.broker.samples.KafkaControllerSampleCollector;
import io.thesis.collector.kafka.jmx.broker.samples.KafkaCoordinatorSampleCollector;
import io.thesis.collector.kafka.jmx.broker.samples.KafkaNetworkSampleCollector;
import io.thesis.collector.kafka.jmx.broker.samples.KafkaServerSampleCollector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.MBeanServerConnection;
import java.util.Map;

import static java.lang.String.format;

/**
 * Collects Apache Kafka server data via the JMX interface. Uses an internal registry
 * of {@code SampleCollector}s and aggregates their results.
 */
public class KafkaBrokerJmxCollector extends AbstractCollector {

    private static final Logger LOG = LoggerFactory.getLogger(KafkaBrokerJmxCollector.class);

    public KafkaBrokerJmxCollector(final MBeanServerConnection mBeanServerConnection) {
        super(kafkaBrokerSampleRegistry(mBeanServerConnection));
    }

    @Override
    protected void checkRegistry() {
        if (getSampleRegistry().isEmpty()) {
            throw new JmxCollectorException(format("No registered %s sample collectors", CollectorType.KAFKA_BROKER_JMX));
        }
    }

    @Override
    public CollectorType getCollectorType() {
        return CollectorType.KAFKA_BROKER_JMX;
    }

    private static Map<String, SampleCollector> kafkaBrokerSampleRegistry(final MBeanServerConnection mBeanServerConnection) {
        final Map<String, SampleCollector> registry = Maps.newHashMap();
        registry.put(KafkaControllerSampleCollector.SAMPLE_KEY, new KafkaControllerSampleCollector(mBeanServerConnection));
        registry.put(KafkaCoordinatorSampleCollector.SAMPLE_KEY, new KafkaCoordinatorSampleCollector(mBeanServerConnection));
        registry.put(KafkaNetworkSampleCollector.SAMPLE_KEY, new KafkaNetworkSampleCollector(mBeanServerConnection));
        registry.put(KafkaServerSampleCollector.SAMPLE_KEY, new KafkaServerSampleCollector(mBeanServerConnection));
        return registry;
    }
}
