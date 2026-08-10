package com.pilotcoupondispatchservice.utils;

import com.pilotcoupondispatchservice.exceptions.InvalidAuthenticationException;
import com.pilotcoupondispatchservice.exceptions.InvalidRequestException;
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

    public static long getLoggedInUserId() {
        try {
            CustomPrincipal principal = (CustomPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return principal.getUserId();
        } catch (Exception e) {
            return 0;
        }
    }

    public static CustomPrincipal getCurrentUserOrThrow() {
        return getCurrentUser()
                .orElseThrow(() -> new InvalidAuthenticationException("No authenticated user in security context"));
    }


    public static CustomPrincipal getLoggedInUser() {
        try {
            return (CustomPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } catch (Exception e) {
            throw new InvalidRequestException("Invalid request. Something went wrong. Principal not found in token.");
        }
    }

    public static boolean isAuthenticated() {
        return getCurrentUser().isPresent();
    }
}
