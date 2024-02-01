package com.example.udd.service.impl;

import com.example.udd.dto.LawDTO;
import com.example.udd.indexmodel.ContractIndex;
import com.example.udd.indexmodel.LawIndex;
import com.example.udd.indexrepository.LawIndexRepository;
import com.example.udd.service.interfaces.FileService;
import com.example.udd.service.interfaces.LawService;
import io.minio.GetObjectResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHitSupport;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.HighlightQuery;
import org.springframework.data.elasticsearch.core.query.highlight.Highlight;
import org.springframework.data.elasticsearch.core.query.highlight.HighlightField;
import org.springframework.data.elasticsearch.core.query.highlight.HighlightFieldParameters;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

import static com.example.udd.util.ExtractDocumentContent.extractDocumentContent;
import static com.example.udd.util.SearchUtils.getCriteriaFromMap;

@Service
@RequiredArgsConstructor
public class LawServiceImpl implements LawService {

    private final LawIndexRepository lawIndexRepository;
    private final FileService fileService;
    private final ElasticsearchOperations elasticsearchTemplate;

    @Override
    public Page<LawDTO> simpleSearch(Map<String, String> criteriaTokens, Pageable pageable) {
        var criteria = getCriteriaFromMap(criteriaTokens);
        if (criteria == null) {
            return Page.empty();
        }

        var highlightFieldParameters = HighlightFieldParameters.builder()
                .withFragmentSize(150)
                .withNumberOfFragments(1)
                .build();

        var searchQueryBuilder =
                new NativeQueryBuilder()
                        .withQuery(new CriteriaQuery(criteria))
                        .withHighlightQuery(new HighlightQuery(
                                new Highlight(List.of(
                                        new HighlightField("content", highlightFieldParameters))),
                                Object.class
                        ))
                        .withPageable(pageable);

        var searchHits = elasticsearchTemplate.search(searchQueryBuilder.build(), LawIndex.class);
        var searchHitsPaged = SearchHitSupport.searchPageFor(searchHits, pageable);

        List<LawDTO> highlightedResults = new ArrayList<>();

        for (SearchHit<LawIndex> searchHit : searchHitsPaged) {
            LawIndex content = searchHit.getContent();
            Map<String, List<String>> highlightFields = searchHit.getHighlightFields();

            highlightedResults.add(new LawDTO(content, highlightFields));
        }

        Page<LawDTO> highlightedPage = new PageImpl<>(highlightedResults, pageable, searchHits.getTotalHits());

        return highlightedPage;
    }

    @Override
    @SneakyThrows
    public String indexDocument(MultipartFile documentFile) {
        var title = Objects.requireNonNull(documentFile.getOriginalFilename()).split("\\.")[0];
        var documentContent = extractDocumentContent(documentFile);
        var serverFilename = fileService.store(documentFile, UUID.randomUUID().toString());

        var law = new LawIndex(title, documentContent, serverFilename);
        lawIndexRepository.save(law);

        return serverFilename;
    }

    @Override
    public GetObjectResponse loadAsResource(final String serverFilename) {
        return fileService.loadAsResource(serverFilename);
    }

}
