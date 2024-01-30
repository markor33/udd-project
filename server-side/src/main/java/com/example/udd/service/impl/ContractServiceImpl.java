package com.example.udd.service.impl;

import com.example.udd.dto.ParsedContractDTO;
import com.example.udd.indexmodel.ContractIndex;
import com.example.udd.indexrepository.ContractIndexRepository;
import com.example.udd.service.interfaces.ContractService;
import com.example.udd.service.interfaces.FileService;
import io.minio.GetObjectResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHitSupport;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.regex.Pattern;

import static com.example.udd.util.ExtractDocumentContent.extractDocumentContent;
import static com.example.udd.util.SearchUtils.getCriteriaFromMap;

@Service
@RequiredArgsConstructor
public class ContractServiceImpl implements ContractService {

    private final ContractIndexRepository contractIndexRepository;
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

        var contract = new ContractIndex(
                title,
                firstName,
                lastName,
                governmentName,
                levelOfAdministration,
                documentContent,
                serverFilename
        );
        contractIndexRepository.save(contract);

        return serverFilename;
    }

    @Override
    public GetObjectResponse loadAsResource(final String serverFilename) {
        return fileService.loadAsResource(serverFilename);
    }

}
