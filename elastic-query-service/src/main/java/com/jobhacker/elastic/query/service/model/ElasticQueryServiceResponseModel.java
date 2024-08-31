package com.jobhacker.elastic.query.service.model;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ElasticQueryServiceResponseModel {
    private String id;
    private String companyName;
    private String role;
    private String location;
    private String link;
    private LocalDate postedAt;
}
