package com.pilotcoupondispatchservice.modules.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OtpVerificationRequest {

    @NotBlank(message = "Email must not be blank")
    private String email;

    @NotBlank(message = "OTP must not be blank")
    private String otp;
}
