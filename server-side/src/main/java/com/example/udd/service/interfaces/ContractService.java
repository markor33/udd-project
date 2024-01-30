package com.example.udd.service.interfaces;

import com.example.udd.dto.ParsedContractDTO;
import io.minio.GetObjectResponse;
import org.springframework.web.multipart.MultipartFile;

public interface ContractService {
    ParsedContractDTO parseDocument(final MultipartFile documentFile);
    String indexDocument(
            final MultipartFile documentFile,
            final String firstName,
            final String lastName,
            final String governmentName,
            final String levelOfAdministration,
            final String street,
            final String number,
            final String city);
    GetObjectResponse loadAsResource(final String serverFilename);
}
