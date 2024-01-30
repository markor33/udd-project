package com.example.udd.indexrepository;

import com.example.udd.indexmodel.ContractIndex;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ContractIndexRepository
    extends ElasticsearchRepository<ContractIndex, UUID> {
}
