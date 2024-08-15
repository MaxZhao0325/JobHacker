package com.jobhacker.job.to.kafka.service.runner.impl;

import com.jobhacker.config.JobToKafkaServiceConfigData;
import com.jobhacker.job.to.kafka.service.listener.NewJobPostedEventPublisher;
import com.jobhacker.job.to.kafka.service.runner.StreamRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.concurrent.Executors;

@Component
@ConditionalOnProperty(name = "job-to-kafka-service.enable-mock-jobs", havingValue = "true")
public class MockKafkaStreamRunner implements StreamRunner {
    private static final Logger LOG = LoggerFactory.getLogger(MockKafkaStreamRunner.class);

    private final JobToKafkaServiceConfigData jobToKafkaServiceConfigData;

    private final NewJobPostedEventPublisher jobPostedEventPublisher;

    private static final Random RANDOM = new Random();

    private static final String[] COMPANIES = new String[]{
            "TechCorp",
            "InnovateX",
            "DevWorks",
            "SoftSolutions",
            "CodeBrew",
            "AlgoSoft",
            "CyberNext",
            "AI Dynamics"
    };

    private static final String[] JOB_TITLES = new String[]{
            "Software Engineer",
            "Data Scientist",
            "DevOps Engineer",
            "Frontend Developer",
            "Backend Developer",
            "Fullstack Developer",
            "Cloud Architect",
            "Product Manager"
    };

    private static final String[] LOCATIONS = new String[]{
            "San Francisco, CA",
            "New York, NY",
            "Austin, TX",
            "Seattle, WA",
            "Chicago, IL",
            "Boston, MA",
            "Los Angeles, CA",
            "Denver, CO"
    };

    public MockKafkaStreamRunner(JobToKafkaServiceConfigData jobToKafkaServiceConfigData, NewJobPostedEventPublisher jobPostedEventPublisher) {
        this.jobToKafkaServiceConfigData = jobToKafkaServiceConfigData;
        this.jobPostedEventPublisher = jobPostedEventPublisher;
    }

    @Override
    public void start() {
        long sleepTimeMs = jobToKafkaServiceConfigData.getMockSleepMs();
        LOG.info("Starting mock job posting stream");
        simulateJobPostingStream(sleepTimeMs);
    }

    private void simulateJobPostingStream(long sleepTimeMs) {
        Executors.newSingleThreadExecutor().submit(() -> {
            try {
                while (true) {
                    generateAndPublishMockJobPosting();
                    sleep(sleepTimeMs);
                }
            } catch (Exception e) {
                LOG.error("Error creating mock job posting!", e);
            }
        });
    }

    private void sleep(long sleepTimeMs) {
        try {
            Thread.sleep(sleepTimeMs);
        } catch (InterruptedException e) {
            throw new RuntimeException("Error while sleeping for waiting new job posting to create!", e);
        }
    }

    private void generateAndPublishMockJobPosting() {
        String jobTitle = JOB_TITLES[RANDOM.nextInt(JOB_TITLES.length)];
        String companyName = COMPANIES[RANDOM.nextInt(COMPANIES.length)];
        String applicationLink = "https://" + companyName + "/jobs.example.com/apply/" + RANDOM.nextInt(1000);
        long postedAt = System.currentTimeMillis();
        String location = LOCATIONS[RANDOM.nextInt(LOCATIONS.length)];

        LOG.info("Generated mock job posting: {} at {}", jobTitle, companyName);

        jobPostedEventPublisher.publishNewJobPostedEvent(companyName, jobTitle, location, applicationLink, Long.toString(postedAt));
    }
}
