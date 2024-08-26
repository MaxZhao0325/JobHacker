package com.jobhacker.elastic.model.index.impl;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.jobhacker.elastic.model.index.IndexModel;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Date;

@Data
@Builder
@Document(indexName = "#{@elasticConfigData.indexName}")
public class JobIndexModel implements IndexModel {

    @JsonProperty
    private String id;
    @JsonProperty
    private String companyName;
    @JsonProperty
    private String role;
    @JsonProperty
    private String location;
    @JsonProperty
    private String link;

    @Field(type = FieldType.Date, format = {}, pattern = "uuuu-MM-dd")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "uuuu-MM-dd")
    @JsonProperty
    private LocalDate postedAt;
}
