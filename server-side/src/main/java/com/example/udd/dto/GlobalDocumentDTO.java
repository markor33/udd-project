package com.example.udd.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GlobalDocumentDTO {

    private UUID id;
    private String _class;
    private String index;
    private String title;
    private String content;
    private String serverFilename;
    private String firstName;
    private String lastName;
    private String governmentName;
    private String levelOfAdministration;
    private GeoPoint location;
    private Map<String, List<String>> highlightFields;
}
