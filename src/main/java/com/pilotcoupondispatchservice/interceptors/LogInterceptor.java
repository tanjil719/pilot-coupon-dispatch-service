package com.pilotcoupondispatchservice.interceptors;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
public class LogInterceptor implements HandlerInterceptor {

    private static final String REQUEST_START_TIME = "request_start_time";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        request.setAttribute(REQUEST_START_TIME, System.currentTimeMillis());
        log.info("Incoming request: {} {}", request.getMethod(), request.getRequestURI());
        return true;
    }

    @Override
    public void afterCompletion(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler,
            Exception ex
    ) {
        long startedAt = (long) request.getAttribute(REQUEST_START_TIME);
        long durationMs = System.currentTimeMillis() - startedAt;

        if (ex == null) {
            log.info(
                    "Outgoing response: {} {} -> {} ({} ms)",
                    request.getMethod(),
                    request.getRequestURI(),
                    response.getStatus(),
                    durationMs
            );
            return;
        }

        log.error(
                "Outgoing response: {} {} -> {} ({} ms), error: {}",
                request.getMethod(),
                request.getRequestURI(),
                response.getStatus(),
                durationMs,
                ex.getMessage()
        );
    }
}
