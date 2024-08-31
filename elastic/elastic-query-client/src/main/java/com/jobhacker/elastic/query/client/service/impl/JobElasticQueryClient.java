package com.jobhacker.elastic.query.client.service.impl;

import com.jobhacker.config.ElasticConfigData;
import com.jobhacker.config.ElasticQueryConfigData;
import com.jobhacker.elastic.model.index.impl.JobIndexModel;
import com.jobhacker.elastic.query.client.exception.ElasticQueryClientException;
import com.jobhacker.elastic.query.client.service.ElasticQueryClient;
import com.jobhacker.elastic.query.client.util.ElasticQueryUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class JobElasticQueryClient implements ElasticQueryClient<JobIndexModel> {

    private static final Logger LOG = LoggerFactory.getLogger(JobElasticQueryClient.class);

    private final ElasticConfigData elasticConfigData;

    private final ElasticQueryConfigData elasticQueryConfigData;

    private final ElasticsearchOperations elasticsearchOperations;

    private final ElasticQueryUtil<JobIndexModel> elasticQueryUtil;

    public JobElasticQueryClient(ElasticConfigData configData,
                                 ElasticQueryConfigData queryConfigData,
                                 ElasticsearchOperations elasticOperations,
                                 ElasticQueryUtil<JobIndexModel> queryUtil) {
        this.elasticConfigData = configData;
        this.elasticQueryConfigData = queryConfigData;
        this.elasticsearchOperations = elasticOperations;
        this.elasticQueryUtil = queryUtil;
    }

    @Override
    public JobIndexModel getIndexModelById(String id) {
        Query query = elasticQueryUtil.getSearchQueryById(id);
        SearchHit<JobIndexModel> searchResult = elasticsearchOperations.searchOne(query, JobIndexModel.class,
                IndexCoordinates.of(elasticConfigData.getIndexName()));
        if (searchResult == null) {
            LOG.error("No document found at elasticsearch with id {}", id);
            throw new ElasticQueryClientException("No document found at elasticsearch with id " + id);
        }
        LOG.info("Document with id {} retrieved successfully", searchResult.getId());
        return searchResult.getContent();
    }

    @Override
    public List<JobIndexModel> getIndexModelByText(String role) {
        // the text field is role here, we can adjust this attribute to search for other fields
        Query query = elasticQueryUtil.getSearchQueryByFieldText(elasticQueryConfigData.getTextField(), role);
        return search(query, "{} of documents with role {} retrieved successfully", role);
    }

    @Override
    public List<JobIndexModel> getAllIndexModels() {
        Query query = elasticQueryUtil.getSearchQueryForAll();
        return search(query, "{} number of documents retrieved successfully");
    }

    private List<JobIndexModel> search(Query query, String logMessage, Object... logParams) {
        SearchHits<JobIndexModel> searchResult = elasticsearchOperations.search(query, JobIndexModel.class,
                IndexCoordinates.of(elasticConfigData.getIndexName()));
        LOG.info(logMessage, searchResult.getTotalHits(), logParams);
        return searchResult.get().map(SearchHit::getContent).collect(Collectors.toList());
    }
}
