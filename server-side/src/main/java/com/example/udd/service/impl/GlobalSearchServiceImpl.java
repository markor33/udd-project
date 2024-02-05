package com.example.udd.service.impl;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import co.elastic.clients.elasticsearch.core.search.Highlight;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.example.udd.dto.DocumentFileDTO;
import com.example.udd.dto.GlobalDocumentDTO;
import com.example.udd.indexmodel.AllDocuments;
import com.example.udd.service.interfaces.GlobalSearchService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GlobalSearchServiceImpl implements GlobalSearchService {

    private final ElasticsearchClient elasticsearchClient;

    @Override
    @SneakyThrows
    public Page<GlobalDocumentDTO> search(final String query, final Pageable pageable) {
        var response = elasticsearchClient.search(s -> s
                        .index("law", "contract")
                        .from(pageable.getPageNumber() * pageable.getPageNumber())
                        .size(pageable.getPageSize())
                        .query(q -> {
                            if (query.isEmpty()) {
                                return q.bool(b -> b.must(QueryBuilders.matchAll().build()._toQuery()));
                            }
                            else {
                                if (query.startsWith("\"") && query.endsWith("\""))
                                    return q.matchPhrase(t -> t.field("content").query(query));
                                else
                                    return q.match(t -> t.field("content").query(query));
                            }
                        })
                        .highlight(h -> h
                                .fields("content", hh -> hh
                                        .matchedFields("content")
                                        .fragmentOffset(150)
                                        .fragmentSize(1))),
                GlobalDocumentDTO.class
        );

        List<GlobalDocumentDTO> documents = response.hits().hits().stream().map(hit -> {
            var global = hit.source();
            global.setIndex(hit.index());
            global.setHighlightFields(hit.highlight());
            return global;
        }).collect(Collectors.toList());
        return new PageImpl<>(documents, pageable, response.hits().total().value());
    }
}
