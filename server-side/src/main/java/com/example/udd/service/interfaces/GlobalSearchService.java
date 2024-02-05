package com.example.udd.service.interfaces;

import com.example.udd.dto.GlobalDocumentDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface GlobalSearchService {
    Page<GlobalDocumentDTO> search(final String query, final Pageable pageable);
}
