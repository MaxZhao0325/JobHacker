package com.jobhacker.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Data
@Configuration
@ConfigurationProperties(prefix = "job-to-kafka-service")
public class JobToKafkaServiceConfigData {
    private List<String> jobKeywords;
    private String welcomeMessage;
    private long mockSleepMs;
    private boolean enableMockJobs;
}
