package com.example.udd.infrastructure.impl;

import com.example.udd.infrastructure.interfaces.LocationIQ;
import com.example.udd.infrastructure.model.Location;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LocationIQImpl implements LocationIQ {

    @Value("${locationiq.api.key}")
    private String locationIQApiKey;
    private final RestTemplate restTemplate = new RestTemplate();
    private final String baseUrl = "https://us1.locationiq.com/v1/search?key=%s&q=%s&format=json";

    @Override
    public Location search(String query) {
        var url = String.format(baseUrl, locationIQApiKey, query);
        var locations = restTemplate.exchange(url,
                HttpMethod.GET, null, new ParameterizedTypeReference<List<Location>>() {
                })
                .getBody();
        return locations.get(0);
    }
}
