package com.pilotcoupondispatchservice.dao;

import com.fasterxml.jackson.databind.JsonNode;
import com.pilotcoupondispatchservice.exceptions.InvalidRequestException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@AllArgsConstructor
public class GoogleLocationServiceImpl implements GoogleLocationService {

    private final RestTemplate restTemplate;

    @Override
    public JsonNode googleLocationSearch(String input, String types, String key) {

        String url = String.format("https://maps.googleapis.com/maps/api/place/autocomplete/json?input=%s&types=%s&key=%s", input, types, key);

        try {
            ResponseEntity<JsonNode> exchange = restTemplate.exchange(url, HttpMethod.GET, null, JsonNode.class);
            log.info("Successfully search from google location input: {} types: {} key: {} status: {} response: {}", input, types, key, exchange.getStatusCode(), exchange.getBody());
            return exchange.getBody();
        } catch (RestClientResponseException e) {
            log.info("Failed to search from google location input: {} types: {} key: {} status: {} error: {}", input, types, key, e.getStatusCode().value(), e.getMessage());
            throw new InvalidRequestException(e.getMessage());
        }

    }

    @Override
    public JsonNode googleLocationSearchById(String id, String key) {

        String url = String.format("https://maps.googleapis.com/maps/api/place/details/json?place_id=%s&key=%s", id, key);

        try {
            ResponseEntity<JsonNode> exchange = restTemplate.exchange(url, HttpMethod.GET, null, JsonNode.class);
            log.info("Successfully search from google location id: {} key: {} status: {} response: {}", id, key, exchange.getStatusCode(), exchange.getBody());
            return exchange.getBody();
        } catch (RestClientResponseException e) {
            log.info("Failed to search from google location id: {} key: {} status: {} error: {}", id, key, e.getStatusCode().value(), e.getMessage());
            throw new InvalidRequestException(e.getMessage());
        }

    }

}
