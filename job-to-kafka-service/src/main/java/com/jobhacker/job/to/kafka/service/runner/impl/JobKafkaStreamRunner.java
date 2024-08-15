package com.jobhacker.job.to.kafka.service.runner.impl;

import com.jobhacker.job.to.kafka.service.runner.StreamRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "job-to-kafka-service.enable-mock-jobs", havingValue = "false", matchIfMissing = true)
public class JobKafkaStreamRunner implements StreamRunner {
    private static final Logger LOG = LoggerFactory.getLogger(JobKafkaStreamRunner.class);

    @Override
    public void start() {
        LOG.info("Real job posting stream start!");
    }
}
