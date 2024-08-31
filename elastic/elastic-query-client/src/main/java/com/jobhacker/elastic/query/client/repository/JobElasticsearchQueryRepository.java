package com.jobhacker.elastic.query.client.repository;

import com.jobhacker.elastic.model.index.impl.JobIndexModel;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobElasticsearchQueryRepository extends ElasticsearchRepository<JobIndexModel, String> {

    List<JobIndexModel> findByRole(String role);
}
