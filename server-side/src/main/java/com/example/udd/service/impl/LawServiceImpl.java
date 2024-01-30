package com.example.udd.service.impl;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import com.example.udd.indexmodel.LawIndex;
import com.example.udd.indexrepository.LawIndexRepository;
import com.example.udd.service.interfaces.LawService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.flogger.Flogger;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

import static com.example.udd.util.ExtractDocumentContent.extractDocumentContent;

@Service
@RequiredArgsConstructor
public class LawServiceImpl implements LawService {

    private final LawIndexRepository lawIndexRepository;
    private final ElasticsearchClient elasticsearchClient;

    @Override
    @SneakyThrows
    public String indexDocument(MultipartFile documentFile) {
        var title = Objects.requireNonNull(documentFile.getOriginalFilename()).split("\\.")[0];
        var documentContent = extractDocumentContent(documentFile);
        var serverFileName = "Server File Name"; // TODO: MiniIO

        var law = new LawIndex(title, documentContent, serverFileName);
        /*IndexResponse response = elasticsearchClient.index(i -> i
                .index("laws")
                .id(law.getId().toString())
                .document(law));
        System.out.println(response.version());*/
        lawIndexRepository.save(law);
        return law.getId().toString();
    }
}
