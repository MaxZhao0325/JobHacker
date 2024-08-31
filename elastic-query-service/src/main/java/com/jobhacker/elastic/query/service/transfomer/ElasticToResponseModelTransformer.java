package com.jobhacker.elastic.query.service.transfomer;

import com.jobhacker.elastic.model.index.impl.JobIndexModel;
import com.jobhacker.elastic.query.service.model.ElasticQueryServiceResponseModel;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ElasticToResponseModelTransformer {

    public ElasticQueryServiceResponseModel getResponseModel(JobIndexModel jobIndexModel) {
        return ElasticQueryServiceResponseModel
                .builder()
                .id(jobIndexModel.getId())
                .companyName(jobIndexModel.getCompanyName())
                .role(jobIndexModel.getRole())
                .location(jobIndexModel.getLocation())
                .link(jobIndexModel.getLink())
                .postedAt(jobIndexModel.getPostedAt())
                .build();
    }

    public List<ElasticQueryServiceResponseModel> getResponseModels(List<JobIndexModel> jobIndexModels) {
        return jobIndexModels.stream().map(this::getResponseModel).collect(Collectors.toList());
    }
}
