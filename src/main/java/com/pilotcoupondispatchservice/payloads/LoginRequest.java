package com.pilotcoupondispatchservice.payloads;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString(exclude = "password")
public class LoginRequest {

    @NotBlank(message = "User-name must not be blank")
    @Email(message = "User-name must be a valid email address")
    @Size(max = 100, message = "User-name must not exceed 100 characters")
    private String username;

    @NotBlank(message = "Password must not be blank")
    @Size(max = 100, message = "Password must not exceed 100 characters")
    private String password;
}
