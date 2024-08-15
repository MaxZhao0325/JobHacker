package com.jobhacker.job.to.kafka.service.transformer;

import com.jobhacker.kafka.avro.model.JobAvroModel;
import org.springframework.stereotype.Component;
import java.util.UUID;

@Component
public class JobToAvroTransformer {

    public JobAvroModel getJobAvroModel(String companyName, String role, String location, String link, String postedAt) {
        String uniqueId = UUID.randomUUID().toString();
        return JobAvroModel
                .newBuilder()
                .setId(uniqueId)
                .setCompanyName(companyName)
                .setRole(role)
                .setLocation(location)
                .setLink(link)
                .setPostedAt(postedAt)
                .build();
    }
}
