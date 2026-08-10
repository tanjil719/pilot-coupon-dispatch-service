package com.pilotcoupondispatchservice.dao;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by IntelliJ IDEA.
 * User: Md. Shamim
 * Date: ১/১২/২২
 * Time: ৮:০২ AM
 * Email: mdshamim723@gmail.com
 */

@Slf4j
@AllArgsConstructor
@Service
public class I18NServiceImpl implements I18NService {

    private final HttpServletRequest httpServletRequest;
    private final MessageSource messageSource;

    @Override
    public String getMessage(String code) {
        try {
            return messageSource.getMessage(code, null, getRequestLocale());
        } catch (Exception e) {
            return "No message found for corresponding locale";
        }
    }

    @Override
    public String getMultipleMessage(List<String> codes) {
        try {
            AtomicReference<String> message = new AtomicReference<>("");
            codes.forEach(code -> message.set(message + messageSource.getMessage(code, null, getRequestLocale()) + " "));
            return message.get();
        } catch (Exception e) {
            return "No message found.";
        }
    }

    @Override
    public byte[] getBytes(String code) {
        try {
            return messageSource.getMessage(code, null, getRequestLocale()).getBytes(StandardCharsets.UTF_8);
        } catch (Exception e) {
            return "No message found.".getBytes();
        }
    }

    private Locale getRequestLocale() {

        try {
            String language = httpServletRequest.getHeader("Accept-Language");
            if (StringUtils.isEmpty(language)) {
                return Locale.getDefault();
            } else {
                return new Locale(language);
            }
        } catch (Exception e) {
            return Locale.getDefault();
        }

    }

}
