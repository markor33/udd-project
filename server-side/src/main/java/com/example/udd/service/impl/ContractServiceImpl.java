package com.example.udd.service.impl;

import com.example.udd.dto.ParsedContractDTO;
import com.example.udd.indexrepository.ContractIndexRepository;
import com.example.udd.service.interfaces.ContractService;
import com.example.udd.service.interfaces.FileService;
import io.minio.GetObjectResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.regex.Pattern;

import static com.example.udd.util.ExtractDocumentContent.extractDocumentContent;

@Service
@RequiredArgsConstructor
public class ContractServiceImpl implements ContractService {

    private final ContractIndexRepository contractIndexRepository;
    private final FileService fileService;

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
    public GetObjectResponse loadAsResource(String serverFilename) {
        return null;
    }

}