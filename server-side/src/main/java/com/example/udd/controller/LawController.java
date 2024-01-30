package com.example.udd.controller;

import com.example.udd.dto.DummyDocumentFileDTO;
import com.example.udd.dto.DummyDocumentFileResponseDTO;
import com.example.udd.service.interfaces.LawService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/law")
@RequiredArgsConstructor
public class LawController {

    private final LawService lawService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DummyDocumentFileResponseDTO addDocumentFile(
            final @ModelAttribute DummyDocumentFileDTO documentFile
    ) {
        var documentId = lawService.indexDocument(documentFile.file());
        return new DummyDocumentFileResponseDTO(documentId);
    }

}
