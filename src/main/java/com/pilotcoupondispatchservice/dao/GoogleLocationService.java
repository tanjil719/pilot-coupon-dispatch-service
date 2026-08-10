package com.pilotcoupondispatchservice.dao;

import com.fasterxml.jackson.databind.JsonNode;

public interface GoogleLocationService {

    public JsonNode googleLocationSearch(String input, String types, String key);

    public JsonNode googleLocationSearchById(String id, String key);

}
