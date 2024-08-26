package com.jobhacker.elastic.index.client.service.impl;

import com.jobhacker.config.ElasticConfigData;
import com.jobhacker.elastic.index.client.service.ElasticIndexClient;
import com.jobhacker.elastic.index.client.util.ElasticIndexUtil;
import com.jobhacker.elastic.model.index.impl.JobIndexModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.IndexedObjectInformation;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@ConditionalOnProperty(name = "elastic-config.is-repository", havingValue = "false")
public class JobElasticIndexClient implements ElasticIndexClient<JobIndexModel> {

    private static final Logger LOG = LoggerFactory.getLogger(JobElasticIndexClient.class);

    private final ElasticConfigData elasticConfigData;

    private final ElasticsearchOperations elasticsearchOperations;

    private final ElasticIndexUtil<JobIndexModel> elasticIndexUtil;

    public JobElasticIndexClient(ElasticConfigData configData,
                                 ElasticsearchOperations elasticOperations,
                                 ElasticIndexUtil<JobIndexModel> indexUtil) {
        this.elasticConfigData = configData;
        this.elasticsearchOperations = elasticOperations;
        this.elasticIndexUtil = indexUtil;
    }

    @Override
    public List<String> save(List<JobIndexModel> documents) {
        List<IndexQuery> indexQueries = elasticIndexUtil.getIndexQueries(documents);
        List<String> documentIds = elasticsearchOperations.bulkIndex(
                indexQueries,
                IndexCoordinates.of(elasticConfigData.getIndexName())
        ).stream().map(IndexedObjectInformation::id).collect(Collectors.toList());
        LOG.info("Documents indexed successfully with type: {} and ids: {}", JobIndexModel.class.getName(),
                documentIds);
        return documentIds;
    }
}