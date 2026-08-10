package com.pilotcoupondispatchservice.modules.auth.dto;

public record TokenPairResponse(
        String accessToken,
        String refreshToken,
        String tokenType
) {}
