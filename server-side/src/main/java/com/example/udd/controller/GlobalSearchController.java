package com.example.udd.controller;

import com.example.udd.dto.GlobalDocumentDTO;
import com.example.udd.request.GlobalSearchRequest;
import com.example.udd.service.interfaces.GlobalSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/global")
@RequiredArgsConstructor
public class GlobalSearchController {

    private final GlobalSearchService globalSearchService;

    @PostMapping("/search")
    @ResponseBody
    public Page<GlobalDocumentDTO> search(
            final @RequestBody GlobalSearchRequest request,
            final Pageable pageable
    ) {
        return globalSearchService.search(request.query(), pageable);
    }


}
