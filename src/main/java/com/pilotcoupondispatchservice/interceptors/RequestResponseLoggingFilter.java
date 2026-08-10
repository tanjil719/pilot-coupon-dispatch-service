package com.pilotcoupondispatchservice.interceptors;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
public class RequestResponseLoggingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request, 1024 * 1024);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);
        long startedAt = System.currentTimeMillis();

        try {
            filterChain.doFilter(requestWrapper, responseWrapper);
        } finally {
            long durationMs = System.currentTimeMillis() - startedAt;
            String requestBody = extractPayload(
                    requestWrapper.getContentAsByteArray(),
                    requestWrapper.getCharacterEncoding(),
                    requestWrapper.getContentType()
            );
            String responseBody = extractPayload(
                    responseWrapper.getContentAsByteArray(),
                    responseWrapper.getCharacterEncoding(),
                    responseWrapper.getContentType()
            );

            log.info(
                    "HTTP {} {} -> {} ({} ms), requestBody: {}, responseBody: {}",
                    request.getMethod(),
                    request.getRequestURI(),
                    responseWrapper.getStatus(),
                    durationMs,
                    requestBody,
                    responseBody
            );

            responseWrapper.copyBodyToResponse();
        }
    }

    private String extractPayload(byte[] body, String encoding, String contentType) {
        if (body == null || body.length == 0) {
            return "";
        }

        if (!isTextBasedContentType(contentType)) {
            return "[binary " + body.length + " bytes]";
        }

        Charset charset = StandardCharsets.UTF_8;
        if (encoding != null && !encoding.isBlank()) {
            try {
                charset = Charset.forName(encoding);
            } catch (Exception ignored) {
                charset = StandardCharsets.UTF_8;
            }
        }

        return new String(body, charset);
    }

    private boolean isTextBasedContentType(String contentType) {
        if (contentType == null || contentType.isBlank()) {
            return true;
        }

        return contentType.startsWith(MediaType.APPLICATION_JSON_VALUE)
                || contentType.startsWith(MediaType.APPLICATION_XML_VALUE)
                || contentType.startsWith(MediaType.TEXT_PLAIN_VALUE)
                || contentType.startsWith(MediaType.TEXT_HTML_VALUE)
                || contentType.startsWith(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                || contentType.startsWith("text/")
                || contentType.contains("+json")
                || contentType.contains("+xml");
    }
}
