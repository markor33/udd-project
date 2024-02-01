package com.example.udd.service.impl;

import co.elastic.clients.elasticsearch._types.query_dsl.*;
import com.example.udd.dto.ContractDTO;
import com.example.udd.dto.ParsedContractDTO;
import com.example.udd.indexmodel.ContractIndex;
import com.example.udd.indexrepository.ContractIndexRepository;
import com.example.udd.infrastructure.interfaces.LocationIQ;
import com.example.udd.service.interfaces.ContractService;
import com.example.udd.service.interfaces.FileService;
import io.minio.GetObjectResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHitSupport;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.CriteriaQueryBuilder;
import org.springframework.data.elasticsearch.core.query.HighlightQuery;
import org.springframework.data.elasticsearch.core.query.highlight.Highlight;
import org.springframework.data.elasticsearch.core.query.highlight.HighlightField;
import org.springframework.data.elasticsearch.core.query.highlight.HighlightFieldParameters;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.regex.Pattern;

import static com.example.udd.util.ExtractDocumentContent.extractDocumentContent;
import static com.example.udd.util.SearchUtils.*;

@Service
@RequiredArgsConstructor
public class ContractServiceImpl implements ContractService {

    private final ContractIndexRepository contractIndexRepository;
    private final LocationIQ locationIQ;
    private final FileService fileService;
    private final ElasticsearchOperations elasticsearchTemplate;

    @Override
    public Page<ContractIndex> simpleSearch(
            final Map<String, String> criteriaTokens,
            final Pageable pageable
    ) {
        var criteria = getCriteriaFromMap(criteriaTokens);
        if (criteria == null) {
            return Page.empty();
        }

        var searchHits = elasticsearchTemplate.search(new CriteriaQuery(criteria), ContractIndex.class);
        var searchHitsPaged = SearchHitSupport.searchPageFor(searchHits, pageable);

        return (Page<ContractIndex>) SearchHitSupport.unwrapSearchHits(searchHitsPaged);
    }

    @Override
    public Page<ContractDTO> advancedSearch(
            final List<String> expression,
            final Pageable pageable
    ) {
        var postfixExpression = convertToPostfix(expression);
        var queryStack = new Stack<Query>();
        for (String token : postfixExpression) {
            if (isOperand(token)) {
                var field = token.split(":")[0];
                var value = token.split(":")[1];
                if (field.equals("location")) {
                    var distance = token.split(":")[2];
                    var location = locationIQ.search(value);
                    queryStack.push(getLocationQuery(location, distance));
                }
                else if (value.startsWith("\"") && value.endsWith("\""))
                    queryStack.push(new MatchPhraseQuery.Builder().field(field).query(value).build()._toQuery());
                else
                    queryStack.push(new MatchQuery.Builder().field(field).query(value).build()._toQuery());
            }
            else if (isOperator(token)) {
                var right = queryStack.pop();
                var left = queryStack.pop();

                var boolQuery = new BoolQuery.Builder();

                switch (token) {
                    case "AND":
                        boolQuery.must(left);
                        boolQuery.must(right);
                        break;
                    case "OR":
                        boolQuery.should(left);
                        boolQuery.should(right);
                        break;
                    case "NOT":
                        boolQuery.must(left);
                        boolQuery.mustNot(right);
                        break;
                }
                queryStack.push(boolQuery.build()._toQuery());
            }
        }

        return runQuery(buildQuery(queryStack.pop(), pageable));
    }

    private Page<ContractDTO> runQuery(final NativeQuery query) {
        var searchHits = elasticsearchTemplate.search(query, ContractIndex.class);
        var searchHitsPaged = SearchHitSupport.searchPageFor(searchHits, query.getPageable());
        var highlightedResults = new ArrayList<ContractDTO>();

        for (SearchHit<ContractIndex> searchHit : searchHitsPaged) {
            ContractIndex content = searchHit.getContent();
            Map<String, List<String>> highlightFields = searchHit.getHighlightFields();

            highlightedResults.add(new ContractDTO(content, highlightFields));
        }

        return new PageImpl<>(highlightedResults, query.getPageable(), searchHits.getTotalHits());
    }

    private NativeQuery buildQuery(final Query query, final Pageable pageable) {
        var highlightFieldParameters = HighlightFieldParameters.builder()
                .withFragmentSize(150)
                .withNumberOfFragments(1)
                .build();

        return new NativeQueryBuilder()
                        .withQuery(query)
                        .withHighlightQuery(new HighlightQuery(
                                new Highlight(List.of(
                                        new HighlightField("content", highlightFieldParameters))),
                                Object.class
                        ))
                        .withPageable(pageable)
                .build();
    }

    private static final Pattern gov = Pattern.compile("Uprava\\s+za\\s+([\\w\\s]+),\\s+nivo\\s+uprave:\\s+([\\w\\s]+),\\s+([\\w\\s]+),\\s+([\\w\\s]+),\\s+([\\w\\s]+),\\s+u\\s+daljem\\s+tekstu\\s+klijent.");
    private static final Pattern signature = Pattern.compile("([\\w]+)\\s+([\\w]+)\\s+([\\w]+)\\s+([\\w]+)\\s+Potpisnik\\s+ugovora\\s+za\\s+klijenta");

    @Override
    public ParsedContractDTO parseDocument(MultipartFile documentFile) {
        var documentContent = extractDocumentContent(documentFile);
        var parsedContract = new ParsedContractDTO();

        var govMatch = gov.matcher(documentContent);
        if (govMatch.find()) {
            parsedContract.setGovernmentName(govMatch.group(1));
            parsedContract.setLevelOfAdministration(govMatch.group(2));
            parsedContract.setStreet(govMatch.group(3));
            parsedContract.setNumber(govMatch.group(4));
            parsedContract.setCit(govMatch.group(5));
        }

        var signatureMatch = signature.matcher(documentContent);
        if (signatureMatch.find()) {
            parsedContract.setFirstName(signatureMatch.group(1));
            parsedContract.setLastName(signatureMatch.group(2));
        }

        return parsedContract;
    }

    @Override
    public String indexDocument(
            final MultipartFile documentFile,
            final String firstName,
            final String lastName,
            final String governmentName,
            final String levelOfAdministration,
            final String street,
            final String number,
            final String city
    ) {
        var title = Objects.requireNonNull(documentFile.getOriginalFilename()).split("\\.")[0];
        var documentContent = extractDocumentContent(documentFile);
        var serverFilename = fileService.store(documentFile, UUID.randomUUID().toString());

        var location = locationIQ.search(String.format("%s %s, %s, Serbia", street, number, city));

        var contract = new ContractIndex(
                title,
                firstName,
                lastName,
                governmentName,
                levelOfAdministration,
                documentContent,
                serverFilename,
                location
        );
        contractIndexRepository.save(contract);

        return serverFilename;
    }

    @Override
    public GetObjectResponse loadAsResource(final String serverFilename) {
        return fileService.loadAsResource(serverFilename);
    }

}
