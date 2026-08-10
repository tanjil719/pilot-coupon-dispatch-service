package com.pilotcoupondispatchservice.dao;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Created by IntelliJ IDEA.
 * User: Md. Shamim
 * Date: ২৫/১১/২২
 * Time: ১০:৫০ PM
 * Email: mdshamim723@gmail.com
 */

public interface GoogleLocationService {

    public JsonNode googleLocationSearch(String input, String types, String key);

    public JsonNode googleLocationSearchById(String id, String key);

}
