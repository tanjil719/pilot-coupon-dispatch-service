package com.pilotcoupondispatchservice.dao;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;


@Slf4j
@Service
@AllArgsConstructor
public class LogServiceImpl implements LogService {

//    private final EventService eventService;

    @Override
    public void logRequest(HttpServletRequest request) {
        Map<String, String> parameters = buildParametersMap(request);
        log.info("Request => url: {}, method: {}, ip: {}, headers: {}, parameters: {}, query: {}", request.getRequestURI(), request.getMethod(), request.getRemoteAddr(), buildHeadersMap(request), parameters, request.getQueryString());
    }

    @Override
    public void logRequestBody(HttpServletRequest request, Object requestBody) {
        String body = String.valueOf(requestBody);
        request.setAttribute("request_payload", body); // Set request body for db event logging while response
        log.info("Request Body => body {}", body);
    }

    @Override
    public void logResponse(HttpServletRequest request, HttpServletResponse response, Object responseBody) {

        /* Todo:
         *   Save only error response payload
         *   Make decision for success response payload
         * */

        String url = request.getRequestURI();
        String method = request.getMethod();

        Object request_payload = request.getAttribute("request_payload");
        String requestPayload = request_payload == null ? null : request_payload.toString();

        String query = request.getQueryString();

        String x_real_ip = request.getHeader("x-real-ip");
        String x_real_host = request.getHeader("host");
        String x_forwarded_for = request.getHeader("x-forwarded-for");

        String remoteAddr = String.format("ip: %s, x-real-ip: %s", request.getRemoteAddr(), x_real_ip);
        int remotePort = request.getRemotePort();
        String remoteHost = String.format("host: %s, x-real-host: %s", request.getRemoteHost(), x_real_host);

        int status = response.getStatus();
        String responsePayload;
        if (status == 200 || status == 201) {
            responsePayload = "Success";
        } else {
            responsePayload = String.valueOf(responseBody);
        }

//        CompletableFuture.runAsync(() -> eventService.saveAndUpdateEvent(
//                new Event(
//                        url,
//                        method,
//                        requestPayload,
//                        query,
//                        remoteAddr,
//                        remotePort,
//                        remoteHost,
//                        status,
//                        responsePayload // save only error response
//                )
//        ));

        log.info("Response => url: {}, method: {}, status: {}, ip: {}, port: {}, host: {}, x-forwarded-for: {}, headers {}, body {}", url, method, status, remoteAddr, remotePort, remoteHost, x_forwarded_for, buildHeadersMap(response), responsePayload);
    }

    private Map<String, String> buildParametersMap(HttpServletRequest request) {

        Map<String, String> result = new HashMap<>();

        Enumeration<String> parameterNames = request.getParameterNames();

        while (parameterNames.hasMoreElements()) {
            String key = parameterNames.nextElement();
            String value = request.getParameter(key);
            result.put(key, value);
        }

        return result;
    }

    private Map<String, String> buildHeadersMap(HttpServletRequest request) {

        Map<String, String> map = new HashMap<>();

        Enumeration<String> headerNames = request.getHeaderNames();

        while (headerNames.hasMoreElements()) {
            String key = headerNames.nextElement();
            String value = request.getHeader(key);
            map.put(key, value);
        }

        return map;
    }

    private Map<String, String> buildHeadersMap(HttpServletResponse response) {
        Map<String, String> map = new HashMap<>();
        response.getHeaderNames().stream().map(key -> map.put(key, response.getHeader(key)));
        return map;
    }

}
