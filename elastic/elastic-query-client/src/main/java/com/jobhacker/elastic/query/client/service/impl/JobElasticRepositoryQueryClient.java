package com.jobhacker.elastic.query.client.service.impl;

import com.jobhacker.common.util.CollectionsUtil;
import com.jobhacker.elastic.model.index.impl.JobIndexModel;
import com.jobhacker.elastic.query.client.exception.ElasticQueryClientException;
import com.jobhacker.elastic.query.client.repository.JobElasticsearchQueryRepository;
import com.jobhacker.elastic.query.client.service.ElasticQueryClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Primary
@Service
public class JobElasticRepositoryQueryClient implements ElasticQueryClient<JobIndexModel> {
    private static final Logger LOG = LoggerFactory.getLogger(JobElasticRepositoryQueryClient.class);

    private final JobElasticsearchQueryRepository jobElasticsearchQueryRepository;

    public JobElasticRepositoryQueryClient(JobElasticsearchQueryRepository repository) {
        this.jobElasticsearchQueryRepository = repository;
    }

    @Override
    public JobIndexModel getIndexModelById(String id) {
        Optional<JobIndexModel> searchResult = jobElasticsearchQueryRepository.findById(id);
        LOG.info("Document with id {} retrieved successfully",
                searchResult.orElseThrow(() ->
                        new ElasticQueryClientException("No document found at elasticsearch with id " + id)).getId());
        return searchResult.get();
    }

    @Override
    public List<JobIndexModel> getIndexModelByText(String text) {
        List<JobIndexModel> searchResult = jobElasticsearchQueryRepository.findByRole(text);
        LOG.info("{} of documents with role {} retrieved successfully", searchResult.size(), text);
        return searchResult;
    }

    @Override
    public List<JobIndexModel> getAllIndexModels() {
        List<JobIndexModel> searchResult =
                CollectionsUtil.getInstance().getListFromIterable(jobElasticsearchQueryRepository.findAll());
        LOG.info("{} number of documents retrieved successfully", searchResult.size());
        return searchResult;
    }
}
