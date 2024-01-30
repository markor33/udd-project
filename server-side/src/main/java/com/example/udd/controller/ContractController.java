package com.example.udd.controller;

import com.example.udd.dto.DummyDocumentFileDTO;
import com.example.udd.dto.ParsedContractDTO;
import com.example.udd.service.interfaces.ContractService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/contract")
@RequiredArgsConstructor
public class ContractController {

    private final ContractService contractService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ParsedContractDTO addDocumentFile(
            final @ModelAttribute DummyDocumentFileDTO documentFile
    ) {
        return contractService.parseDocument(documentFile.file());
    }
}
