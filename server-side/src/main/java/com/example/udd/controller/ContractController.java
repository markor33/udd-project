package com.example.udd.controller;

import com.example.udd.dto.*;
import com.example.udd.indexmodel.ContractIndex;
import com.example.udd.service.interfaces.ContractService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import static com.example.udd.util.SearchUtils.convertToPostfix;

@RestController
@RequestMapping("/contract")
@RequiredArgsConstructor
public class ContractController {

    private final ContractService contractService;

    @PostMapping("/simple-search")
    @ResponseBody
    public Page<ContractIndex> simpleSearch(
            final @RequestBody Map<String, String> criteriaTokens,
            final Pageable pageable
    ) {
        return contractService.simpleSearch(criteriaTokens, pageable);
    }

    @PostMapping("/advanced-search")
    @ResponseBody
    public Page<ContractDTO> advancedSearch(
            final @RequestBody AdvancedSearchRequestDTO request,
            final Pageable pageable
    ) {
        return contractService.advancedSearch(request.expression(), pageable);
    }

    @GetMapping("/file/{filename}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable final String filename) throws IOException {

        var minioResponse = contractService.loadAsResource(filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        minioResponse.headers().get("Content-Disposition"))
                .header(HttpHeaders.CONTENT_TYPE, Files.probeContentType(Path.of(filename)))
                .body(new InputStreamResource(minioResponse));
    }

    @PostMapping("/parsed")
    @ResponseStatus(HttpStatus.CREATED)
    public ParsedContractDTO getParsedContract(
            final @ModelAttribute DummyDocumentFileDTO documentFile
    ) {
        return contractService.parseDocument(documentFile.file());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DummyDocumentFileResponseDTO addDocumentFile(
            final @ModelAttribute IndexContractRequestDTO request
    ) {
        return new DummyDocumentFileResponseDTO(contractService.indexDocument(
                request.file(),
                request.firstName(),
                request.lastName(),
                request.governmentName(),
                request.levelOfAdministration(),
                request.street(),
                request.number(),
                request.city()
        ));
    }

}
