package com.jobhacker.kafka.to.elastic.service.consumer.impl;

import com.jobhacker.config.KafkaConfigData;
import com.jobhacker.config.KafkaConsumerConfigData;
import com.jobhacker.elastic.index.client.service.ElasticIndexClient;
import com.jobhacker.elastic.model.index.impl.JobIndexModel;
import com.jobhacker.kafka.admin.client.KafkaAdminClient;
import com.jobhacker.kafka.avro.model.JobAvroModel;
import com.jobhacker.kafka.to.elastic.service.consumer.KafkaConsumer;
import com.jobhacker.kafka.to.elastic.service.transformer.AvroToElasticModelTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.annotation.KafkaListener;

import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class JobKafkaConsumer implements KafkaConsumer<JobAvroModel> {

    private static final Logger LOG = LoggerFactory.getLogger(JobKafkaConsumer.class);

    private final KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;

    private final KafkaAdminClient kafkaAdminClient;

    private final KafkaConfigData kafkaConfigData;

    private final KafkaConsumerConfigData kafkaConsumerConfigData;

    private final AvroToElasticModelTransformer avroToElasticModelTransformer;

    private final ElasticIndexClient<JobIndexModel> elasticIndexClient;

    public JobKafkaConsumer(KafkaListenerEndpointRegistry listenerEndpointRegistry,
                            KafkaAdminClient adminClient,
                            KafkaConfigData configData, KafkaConsumerConfigData kafkaConsumerConfigData, AvroToElasticModelTransformer avroToElasticModelTransformer, ElasticIndexClient<JobIndexModel> elasticIndexClient) {
        this.kafkaListenerEndpointRegistry = listenerEndpointRegistry;
        this.kafkaAdminClient = adminClient;
        this.kafkaConfigData = configData;
        this.kafkaConsumerConfigData = kafkaConsumerConfigData;
        this.avroToElasticModelTransformer = avroToElasticModelTransformer;
        this.elasticIndexClient = elasticIndexClient;
    }

    @EventListener
    public void onAppStarted(ApplicationStartedEvent event) {
        kafkaAdminClient.checkTopicsCreated();
        LOG.info("Topics with name {} is ready for operations!", kafkaConfigData.getTopicNamesToCreate().toArray());
        Objects.requireNonNull(kafkaListenerEndpointRegistry.getListenerContainer(kafkaConsumerConfigData.getConsumerGroupId())).start();
    }

    @Override
    @KafkaListener(id = "${kafka-consumer-config.consumer-group-id}", topics = "${kafka-config.topic-name}")
    public void receive(@Payload List<JobAvroModel> messages,
                        @Header(KafkaHeaders.RECEIVED_KEY) List<Long> keys,
                        @Header(KafkaHeaders.RECEIVED_PARTITION) List<Integer> partitions,
                        @Header(KafkaHeaders.OFFSET) List<Long> offsets) {
        LOG.info("{} number of message received with keys {}, partitions {} and offsets {}, " +
                        "sending it to elastic: Thread id {}",
                messages.size(),
                keys.toString(),
                partitions.toString(),
                offsets.toString(),
                Thread.currentThread().getId());
        List<JobIndexModel> jobIndexModels = avroToElasticModelTransformer.getElasticModels(messages);
        List<String> documentIds = elasticIndexClient.save(jobIndexModels);
        LOG.info("Documents saved to elasticsearch with ids {}", documentIds.toArray());
    }
}
