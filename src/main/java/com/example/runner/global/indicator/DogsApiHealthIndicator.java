package com.example.runner.global.indicator;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

// Website health check를 연동
@Component
public class DogsApiHealthIndicator implements HealthIndicator {
    private static final String DOGS_API_URL = "https://dog.ceo/api/breeds/image/random";

    @Override
    public Health health() {
        try {
            ParameterizedTypeReference<Map<String, String>> reference =
                    new ParameterizedTypeReference<>() {
                    };

            ResponseEntity<Map<String, String>> result =
                    new RestTemplate().exchange(DOGS_API_URL, HttpMethod.GET, null, reference);

            if (result.getStatusCode().is2xxSuccessful() && result.getBody() != null) {
                return Health.up().withDetails(result.getBody()).build();
            } else {
                return Health.down().withDetail("status", result.getStatusCode()).build();
            }
        } catch (RestClientException e) {
            return Health.down().withException(e).build();
        }
    }
}
