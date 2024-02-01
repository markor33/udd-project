package com.example.udd.dto;

import com.example.udd.indexmodel.LawIndex;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
public class LawDTO {

    private LawIndex content;
    private Map<String, List<String>> highlightFields;
}
