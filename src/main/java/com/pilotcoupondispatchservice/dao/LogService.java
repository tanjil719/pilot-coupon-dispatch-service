package com.pilotcoupondispatchservice.dao;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface LogService {

    public void logRequest(HttpServletRequest request);

    public void logRequestBody(HttpServletRequest request, Object requestBody);

    public void logResponse(HttpServletRequest request, HttpServletResponse response, Object responseBody);

}
