package com.example.udd.util;

import org.springframework.data.elasticsearch.core.query.Criteria;

import java.util.Map;

public class SearchUtils {

    public static Criteria getCriteriaFromMap(final Map<String, String> map) {
        return map.entrySet().stream()
                .filter(entry -> !entry.getValue().isEmpty())
                .map(entry -> new Criteria(entry.getKey()).is(entry.getValue()))
                .reduce(Criteria::and)
                .orElse(null);
    }
}
