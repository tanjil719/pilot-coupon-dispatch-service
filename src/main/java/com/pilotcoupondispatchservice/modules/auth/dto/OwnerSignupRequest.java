package com.pilotcoupondispatchservice.modules.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OwnerSignupRequest {

    @NotBlank(message = "Name must not be blank")
    @Size(max = 100, message = "Name must not exceed 100 characters")
    private String name;

    @NotBlank(message = "Email must not be blank")
    @Email(message = "Email must be a valid email address")
    @Size(max = 100, message = "Email must not exceed 100 characters")
    private String email;

    @NotBlank(message = "Phone must not be blank")
    @Pattern(regexp = "^01[3-9]\\d{8}$", message = "Phone must be a valid Bangladeshi mobile number (e.g. 01712345678)")
    private String phone;

    @NotBlank(message = "Password must not be blank")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^a-zA-Z0-9]).{8,100}$",
            message = "Password must be 8-100 characters long and include at least one uppercase letter, one lowercase letter, one digit, and one special character"
    )
    private String password;
}
