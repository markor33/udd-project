package com.example.udd.service.interfaces;

import com.example.udd.dto.ParsedContractDTO;
import io.minio.GetObjectResponse;
import org.springframework.web.multipart.MultipartFile;

public interface ContractService {
    ParsedContractDTO parseDocument(final MultipartFile documentFile);
    GetObjectResponse loadAsResource(final String serverFilename);
}
