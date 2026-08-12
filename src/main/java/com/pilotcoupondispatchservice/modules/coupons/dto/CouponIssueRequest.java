package com.pilotcoupondispatchservice.modules.coupons.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CouponIssueRequest {


    @Positive(message = "Amount must be greater than 0")
    private Double amount;

//    private LocalDateTime expiresAt;

    @NotNull(message = "Validity days must not be null")
    @Positive(message = "Validity days must be greater than 0")
    private Integer validityDays;

    @Size(max = 500, message = "Note must not exceed 500 characters")
    private String note;
}
