package com.jobhacker.elastic.index.client.service;

import com.jobhacker.elastic.model.index.IndexModel;

import java.util.List;

public interface ElasticIndexClient<T extends IndexModel> {
    List<String> save(List<T> documents);
}
