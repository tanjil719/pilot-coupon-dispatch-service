package com.pilotcoupondispatchservice.payloads;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CustomPrincipal(
        String userId,
        String email,
        String fullName,
        String role
) {}
