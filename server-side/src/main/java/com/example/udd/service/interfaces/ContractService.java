package com.example.udd.service.interfaces;

import com.example.udd.dto.ParsedContractDTO;
import com.example.udd.indexmodel.ContractIndex;
import io.minio.GetObjectResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface ContractService {
    Page<ContractIndex> searchByFirstAndLastName(final String firstName, final String lastName, Pageable pageable);
    ParsedContractDTO parseDocument(final MultipartFile documentFile);
    GetObjectResponse loadAsResource(final String serverFilename);
}
