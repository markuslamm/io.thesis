package io.thesis.collector.client.outbound;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;

/**
 * {@code OutboundWriter} implementation for writing collected data into a Apache Kafka topic.
 */
public class KafkaOutboundWriter implements OutboundWriter {

    private static final Logger LOG = LoggerFactory.getLogger(KafkaOutboundWriter.class);

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final String kafkaOutboundTopic;

    public KafkaOutboundWriter(final KafkaTemplate<String, String> kafkaTemplate, final String kafkaOutboundTopic) {
        this.kafkaTemplate = requireNonNull(kafkaTemplate);
        this.kafkaOutboundTopic = requireNonNull(kafkaOutboundTopic);
    }

    /**
     * Send data into Kafka topic with {@code KafkaTemplate}.
     *
     * @param jsonData collected data JSON formatted
     */
    @Override
    public void write(final String key, final String jsonData) {
        LOG.debug("Trying to send data to Kafka");
        final ListenableFuture<SendResult<String, String>> sendResultFuture =
                kafkaTemplate.send(kafkaOutboundTopic, key, jsonData);
        sendResultFuture.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
            @Override
            public void onSuccess(final SendResult<String, String> response) {
                //just for info
                LOG.debug("Successfully send data to Kafka, topic={}, key={}", kafkaOutboundTopic, key);
            }

            @Override
            public void onFailure(final Throwable ex) {
                final OutboundWriterException exception =
                        new OutboundWriterException(format("Error sending data to Kafka: %s", ex.getMessage()));
                LOG.warn(exception.getMessage());
                throw exception;
            }
        });
    }
}
