package com.pilotcoupondispatchservice.modules.coupons.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class CouponRequestCreateRequest {

    @NotBlank(message = "Route code must not be blank")
    @Size(max = 30, message = "Route code must not exceed 30 characters")
    private String routeCode;

    @NotNull(message = "Service start must not be null")
    private LocalDateTime serviceStart;

    @Size(max = 500, message = "Note must not exceed 500 characters")
    private String note;
}
