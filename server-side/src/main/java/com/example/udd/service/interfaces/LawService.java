package com.example.udd.service.interfaces;

import io.minio.GetObjectResponse;
import org.springframework.web.multipart.MultipartFile;

public interface LawService {
    String indexDocument(final MultipartFile documentFile);
    GetObjectResponse loadAsResource(final String serverFilename);
}
