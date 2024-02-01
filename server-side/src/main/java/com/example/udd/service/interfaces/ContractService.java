package com.example.udd.service.interfaces;

import com.example.udd.dto.ContractDTO;
import com.example.udd.dto.ParsedContractDTO;
import com.example.udd.indexmodel.ContractIndex;
import io.minio.GetObjectResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface ContractService {
    Page<ContractIndex> simpleSearch(
            final Map<String, String> criteria,
            final Pageable pageable);
    Page<ContractDTO> advancedSearch(
            final List<String> expression,
            final Pageable pageable);
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
