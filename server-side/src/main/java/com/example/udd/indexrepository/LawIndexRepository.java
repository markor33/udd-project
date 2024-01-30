package com.example.udd.indexrepository;

import com.example.udd.indexmodel.LawIndex;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface LawIndexRepository
    extends ElasticsearchRepository<LawIndex, UUID> {
}
