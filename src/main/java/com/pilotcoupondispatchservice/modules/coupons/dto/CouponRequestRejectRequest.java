package com.pilotcoupondispatchservice.modules.coupons.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CouponRequestRejectRequest {

    @NotBlank(message = "Reason must not be blank")
    @Size(max = 500, message = "Reason must not exceed 500 characters")
    private String reason;
}
