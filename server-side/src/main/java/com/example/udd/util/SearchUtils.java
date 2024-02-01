package com.example.udd.util;

import co.elastic.clients.elasticsearch._types.query_dsl.GeoDistanceQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import com.example.udd.dto.ContractDTO;
import com.example.udd.indexmodel.ContractIndex;
import com.example.udd.infrastructure.model.Location;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.Criteria;

import java.util.*;

public class SearchUtils {

    private static final Map<String, Integer> priorities = Map.of("NOT", 3, "AND", 2, "OR", 1);

    public static Criteria getCriteriaFromMap(final Map<String, String> map) {
        return map.entrySet().stream()
                .filter(entry -> !entry.getValue().isEmpty())
                .map(entry -> new Criteria(entry.getKey()).is(entry.getValue()))
                .reduce(Criteria::and)
                .orElse(null);
    }

    public static Query getLocationQuery(final Location location, final String distance) {
        return new GeoDistanceQuery.Builder()
                .field("location")
                .distance(String.format("%skm", distance))
                .location(geoLocation -> geoLocation
                        .latlon(latLonGeoLocation -> latLonGeoLocation
                                .lat(location.getLat())
                                .lon(location.getLon())))
                .build()._toQuery();
    }

    public static List<String> convertToPostfix(final List<String> expression) {
        var postfix = new ArrayList<String>();
        var stack = new Stack<String>();

        for (String token : expression) {
            if (isOperand(token)) {
                postfix.add(token);
            } else if (isOperator(token)) {
                while (!stack.isEmpty() && priorities.get(token) <= priorities.get(stack.peek())) {
                    postfix.add(stack.pop());
                }
                stack.push(token);
            }
        }

        while (!stack.isEmpty()) {
            postfix.add(stack.pop());
        }

        return postfix;
    }

    public static boolean isOperand(String token) {
        return !Arrays.asList("AND", "OR", "NOT").contains(token);
    }

    public static boolean isOperator(String token) {
        return Arrays.asList("AND", "OR", "NOT").contains(token);
    }
}
