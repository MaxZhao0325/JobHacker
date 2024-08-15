package com.jobhacker.kafka.producer.config.service.impl;

import com.jobhacker.kafka.avro.model.JobAvroModel;
import com.jobhacker.kafka.producer.config.service.KafkaProducer;
import jakarta.annotation.PreDestroy;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

@Service
public class JobKafkaProducer implements KafkaProducer<Long, JobAvroModel> {

    private static final Logger LOG = LoggerFactory.getLogger(JobKafkaProducer.class);

    private KafkaTemplate<Long, JobAvroModel> kafkaTemplate;

    public JobKafkaProducer(KafkaTemplate<Long, JobAvroModel> template) {
        this.kafkaTemplate = template;
    }

    @Override
    public void send(String topicName, Long key, JobAvroModel message) {
        LOG.info("Sending message='{}' to topic='{}'", message, topicName);
        CompletableFuture<SendResult<Long, JobAvroModel>> kafkaResultFuture = kafkaTemplate.send(topicName, key, message);
        kafkaResultFuture.whenComplete(getCallback(topicName, message));
    }

    @PreDestroy
    public void close() {
        if (kafkaTemplate != null) {
            LOG.info("Closing kafka producer!");
            kafkaTemplate.destroy();
        }
    }

    private BiConsumer<SendResult<Long, JobAvroModel>, Throwable> getCallback(String topicName, JobAvroModel message) {
        return (result, ex) -> {
            if (ex == null) {
                RecordMetadata metadata = result.getRecordMetadata();
                LOG.info("Received new metadata. Topic: {}; Partition {}; Offset {}; Timestamp {}, at time {}",
                        metadata.topic(),
                        metadata.partition(),
                        metadata.offset(),
                        metadata.timestamp(),
                        System.nanoTime());
            } else {
                LOG.error("Error while sending message {} to topic {}", message.toString(), topicName, ex);
            }
        };
    }
}
