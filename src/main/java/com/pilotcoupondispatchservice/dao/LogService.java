package com.pilotcoupondispatchservice.dao;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Created by IntelliJ IDEA.
 * User: Md. Shamim
 * Date: ২৫/১১/২২
 * Time: ১০:৫০ PM
 * Email: mdshamim723@gmail.com
 */

public interface LogService {

    public void logRequest(HttpServletRequest request);

    public void logRequestBody(HttpServletRequest request, Object requestBody);

    public void logResponse(HttpServletRequest request, HttpServletResponse response, Object responseBody);

}
