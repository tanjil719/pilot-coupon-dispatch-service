package com.pilotcoupondispatchservice.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;
import java.util.Objects;

@ConfigurationProperties(prefix = "jwt")
public record JwtProperties(
        String secret,
        Duration accessTokenExpiration,
        Duration refreshTokenExpiration
) {
    public JwtProperties {
        Objects.requireNonNull(secret, "jwt.secret must not be null");
        Objects.requireNonNull(accessTokenExpiration, "jwt.access-token-expiration must not be null");
        Objects.requireNonNull(refreshTokenExpiration, "jwt.refresh-token-expiration must not be null");
    }
}
