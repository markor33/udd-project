package com.example.udd.service.interfaces;

import com.example.udd.indexmodel.LawIndex;
import io.minio.GetObjectResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface LawService {
    Page<LawIndex> simpleSearch(
            final Map<String, String> criteria,
            final Pageable pageable);
    String indexDocument(final MultipartFile documentFile);
    GetObjectResponse loadAsResource(final String serverFilename);
}
