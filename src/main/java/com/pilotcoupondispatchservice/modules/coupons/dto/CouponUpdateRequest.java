package com.pilotcoupondispatchservice.modules.coupons.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CouponUpdateRequest {

    @NotNull(message = "Amount must not be null")
    @Positive(message = "Amount must be greater than 0")
    private Double amount;

    @NotNull(message = "ExpiresAt must not be null")
    private LocalDateTime expiresAt;
}
