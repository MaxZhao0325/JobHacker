package com.jobhacker.elastic.query.service.business.impl;

import com.jobhacker.elastic.model.index.impl.JobIndexModel;
import com.jobhacker.elastic.query.client.service.ElasticQueryClient;
import com.jobhacker.elastic.query.service.business.ElasticQueryService;
import com.jobhacker.elastic.query.service.model.ElasticQueryServiceResponseModel;

import com.jobhacker.elastic.query.service.transfomer.ElasticToResponseModelTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobElasticQueryService implements ElasticQueryService {

    private static final Logger LOG = LoggerFactory.getLogger(JobElasticQueryService.class);

    private final ElasticToResponseModelTransformer elasticToResponseModelTransformer;

    private final ElasticQueryClient<JobIndexModel> elasticQueryClient;

    public JobElasticQueryService(ElasticToResponseModelTransformer transformer,
                                  ElasticQueryClient<JobIndexModel> queryClient) {
        this.elasticToResponseModelTransformer = transformer;
        this.elasticQueryClient = queryClient;
    }

    @Override
    public ElasticQueryServiceResponseModel getDocumentById(String id) {
        LOG.info("Querying elasticsearch by id {}", id);
        return elasticToResponseModelTransformer.getResponseModel(elasticQueryClient.getIndexModelById(id));
    }

    @Override
    public List<ElasticQueryServiceResponseModel> getDocumentByText(String text) {
        LOG.info("Querying elasticsearch by text {}", text);
        return elasticToResponseModelTransformer.getResponseModels(elasticQueryClient.getIndexModelByText(text));
    }

    @Override
    public List<ElasticQueryServiceResponseModel> getAllDocuments() {
        LOG.info("Querying all documents in elasticsearch");
        return elasticToResponseModelTransformer.getResponseModels(elasticQueryClient.getAllIndexModels());
    }
}
