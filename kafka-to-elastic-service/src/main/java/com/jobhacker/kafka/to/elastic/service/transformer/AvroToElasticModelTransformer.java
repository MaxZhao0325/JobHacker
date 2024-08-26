package com.jobhacker.kafka.to.elastic.service.transformer;

import com.jobhacker.elastic.model.index.impl.JobIndexModel;
import com.jobhacker.kafka.avro.model.JobAvroModel;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class AvroToElasticModelTransformer {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("uuuu-MM-dd");

    public List<JobIndexModel> getElasticModels(List<JobAvroModel> avroModels) {
        return avroModels.stream()
                .map(this::transformToElasticModel)
                .collect(Collectors.toList());
    }

    private JobIndexModel transformToElasticModel(JobAvroModel avroModel) {
        // Parse the formatted date string back to LocalDate
        LocalDate date = LocalDate.parse(avroModel.getPostedAt(), DATE_FORMATTER);

        return JobIndexModel.builder()
                .id(avroModel.getId())
                .companyName(avroModel.getCompanyName())
                .role(avroModel.getRole())
                .location(avroModel.getLocation())
                .link(avroModel.getLink())
                .postedAt(date)
                .build();
    }
}