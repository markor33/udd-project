package com.example.udd.service.interfaces;

import org.springframework.web.multipart.MultipartFile;

public interface LawService {
    String indexDocument(final MultipartFile documentFile);
}
