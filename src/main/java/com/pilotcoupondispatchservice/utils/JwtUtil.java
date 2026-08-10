package com.pilotcoupondispatchservice.utils;

import com.pilotcoupondispatchservice.config.JwtProperties;
import com.pilotcoupondispatchservice.payloads.CustomPrincipal;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtUtil {

    private static final String TOKEN_TYPE_CLAIM = "tokenType";
    private static final String ACCESS_TOKEN_TYPE = "access";
    private static final String REFRESH_TOKEN_TYPE = "refresh";

    private final JwtProperties jwtProperties;

    private SecretKey signingKey;

    @PostConstruct
    void init() {
        this.signingKey = Keys.hmacShaKeyFor(java.util.Base64.getDecoder().decode(jwtProperties.secret()));
    }

    public String generateAccessToken(CustomPrincipal principal) {
        return Jwts.builder()
                .id(UUID.randomUUID().toString())
                .subject(principal.userId())
                .claim("email", principal.email())
                .claim("fullName", principal.fullName())
                .claim("role", principal.role())
                .claim(TOKEN_TYPE_CLAIM, ACCESS_TOKEN_TYPE)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtProperties.accessTokenExpiration().toMillis()))
                .signWith(signingKey, Jwts.SIG.HS256)
                .compact();
    }

    public String generateRefreshToken(CustomPrincipal principal) {
        return Jwts.builder()
                .id(UUID.randomUUID().toString())
                .subject(principal.userId())
                .claim("email", principal.email())
                .claim("fullName", principal.fullName())
                .claim("role", principal.role())
                .claim(TOKEN_TYPE_CLAIM, REFRESH_TOKEN_TYPE)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtProperties.refreshTokenExpiration().toMillis()))
                .signWith(signingKey, Jwts.SIG.HS256)
                .compact();
    }

    public CustomPrincipal extractPrincipal(String token) {
        var claims = extractAllClaims(token);
        return new CustomPrincipal(
                claims.getSubject(),
                claims.get("email", String.class),
                claims.get("fullName", String.class),
                claims.get("role", String.class)
        );
    }

    public String extractUserId(String token) {
        return extractAllClaims(token).getSubject();
    }

    public boolean isTokenValid(String token) {
        try {
            extractAllClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            log.debug("JWT validation failed: {}", e.getMessage());
            return false;
        }
    }

    public boolean isAccessToken(String token) {
        return ACCESS_TOKEN_TYPE.equals(extractAllClaims(token).get(TOKEN_TYPE_CLAIM, String.class));
    }

    public boolean isRefreshToken(String token) {
        return REFRESH_TOKEN_TYPE.equals(extractAllClaims(token).get(TOKEN_TYPE_CLAIM, String.class));
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
