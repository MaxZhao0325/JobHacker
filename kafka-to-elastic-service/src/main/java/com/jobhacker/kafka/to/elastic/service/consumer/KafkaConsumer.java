package com.jobhacker.kafka.to.elastic.service.consumer;

import org.apache.avro.specific.SpecificRecordBase;

import java.util.List;

public interface KafkaConsumer<T extends SpecificRecordBase> {
    void receive(List<T> messages, List<Long> keys, List<Integer> partitions, List<Long> offsets);
}