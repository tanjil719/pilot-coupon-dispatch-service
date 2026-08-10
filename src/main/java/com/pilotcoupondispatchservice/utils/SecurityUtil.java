package com.pilotcoupondispatchservice.utils;

import com.pilotcoupondispatchservice.exceptions.InvalidAuthenticationException;
import com.pilotcoupondispatchservice.payloads.CustomPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public final class SecurityUtil {

    private SecurityUtil() {}

    public static Optional<CustomPrincipal> getCurrentUser() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .filter(Authentication::isAuthenticated)
                .map(Authentication::getPrincipal)
                .filter(CustomPrincipal.class::isInstance)
                .map(CustomPrincipal.class::cast);
    }

    public static CustomPrincipal getCurrentUserOrThrow() {
        return getCurrentUser()
                .orElseThrow(() -> new InvalidAuthenticationException("No authenticated user in security context"));
    }

    public static String getCurrentUserId() {
        return getCurrentUserOrThrow().userId();
    }

    public static String getLoggedInUserPermission() {
        return getCurrentUser()
                .map(CustomPrincipal::role)
                .orElse("");
    }

    public static boolean isAuthenticated() {
        return getCurrentUser().isPresent();
    }
}
