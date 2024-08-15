package com.jobhacker.job.to.kafka.service;

import com.jobhacker.config.JobToKafkaServiceConfigData;
import com.jobhacker.job.to.kafka.service.init.StreamInitializer;
import com.jobhacker.job.to.kafka.service.runner.StreamRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.ComponentScan;

import java.util.Arrays;

@SpringBootApplication
@ComponentScan(basePackages = "com.jobhacker")
public class JobToKafkaServiceApplication implements CommandLineRunner {
    private static final Logger LOG = LoggerFactory.getLogger(JobToKafkaServiceApplication.class);

    private final StreamRunner streamRunner;

    private final StreamInitializer streamInitializer;

    public JobToKafkaServiceApplication(StreamRunner runner, StreamInitializer initializer) {
        this.streamRunner = runner;
        this.streamInitializer = initializer;
    }

    public static void main(String[] args) {
        SpringApplication.run(JobToKafkaServiceApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        streamInitializer.init();
        streamRunner.start();
    }
}
