package com.jobhacker.job.to.kafka.service.listener;

import com.jobhacker.config.KafkaConfigData;
import com.jobhacker.kafka.avro.model.JobAvroModel;
import com.jobhacker.kafka.producer.config.service.KafkaProducer;
import com.jobhacker.job.to.kafka.service.transformer.JobToAvroTransformer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.zip.CRC32;

@Component
public class NewJobPostedEventPublisher {
    private static final Logger LOG = LoggerFactory.getLogger(NewJobPostedEventPublisher.class);

    private final KafkaConfigData kafkaConfigData;

    private final KafkaProducer<Long, JobAvroModel> kafkaProducer;

    private final JobToAvroTransformer JobToAvroTransformer;

    public NewJobPostedEventPublisher(KafkaConfigData configData, KafkaProducer<Long, JobAvroModel> producer, JobToAvroTransformer transformer) {
        this.kafkaConfigData = configData;
        this.kafkaProducer = producer;
        this.JobToAvroTransformer = transformer;
    }

    public long hashStringToLong(String companyName) {
        CRC32 crc = new CRC32();
        crc.update(companyName.getBytes(StandardCharsets.UTF_8));
        return crc.getValue();
    }

    public void publishNewJobPostedEvent(String companyName, String role, String location, String link, String postedAt) {
        String jobInfo = companyName + "-" + role + "-" + location + "-" + link + "-" + postedAt;
        LOG.info("Received job post {} sending to kafka topic {}", jobInfo, kafkaConfigData.getTopicName());

        JobAvroModel jobAvroModel = JobToAvroTransformer.getJobAvroModel(companyName, role, location, link, postedAt);
        kafkaProducer.send(kafkaConfigData.getTopicName(), hashStringToLong(jobAvroModel.getCompanyName()), jobAvroModel);
    }
}
