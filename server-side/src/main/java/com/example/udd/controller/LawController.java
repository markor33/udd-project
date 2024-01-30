package com.example.udd.controller;

import com.example.udd.dto.DummyDocumentFileDTO;
import com.example.udd.dto.DummyDocumentFileResponseDTO;
import com.example.udd.service.interfaces.FileService;
import com.example.udd.service.interfaces.LawService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@RestController
@RequestMapping("/law")
@RequiredArgsConstructor
public class LawController {

    private final LawService lawService;

    @GetMapping("/file/{filename}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable final String filename) throws IOException {

        var minioResponse = lawService.loadAsResource(filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        minioResponse.headers().get("Content-Disposition"))
                .header(HttpHeaders.CONTENT_TYPE, Files.probeContentType(Path.of(filename)))
                .body(new InputStreamResource(minioResponse));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DummyDocumentFileResponseDTO addDocumentFile(
            final @ModelAttribute DummyDocumentFileDTO documentFile
    ) {
        return new DummyDocumentFileResponseDTO(lawService.indexDocument(documentFile.file()));
    }

}
