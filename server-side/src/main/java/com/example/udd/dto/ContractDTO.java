package com.example.udd.dto;

import com.example.udd.indexmodel.ContractIndex;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
public class ContractDTO {

    private ContractIndex content;
    private Map<String, List<String>> highlightFields;



}
