package com.example.udd.service.impl;

import com.example.udd.indexmodel.LawIndex;
import com.example.udd.indexrepository.LawIndexRepository;
import com.example.udd.service.interfaces.FileService;
import com.example.udd.service.interfaces.LawService;
import io.minio.GetObjectResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;
import java.util.UUID;

import static com.example.udd.util.ExtractDocumentContent.extractDocumentContent;

@Service
@RequiredArgsConstructor
public class LawServiceImpl implements LawService {

    private final LawIndexRepository lawIndexRepository;
    private final FileService fileService;

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
