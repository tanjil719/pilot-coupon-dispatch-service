package com.pilotcoupondispatchservice.payloads;

import jakarta.validation.constraints.NotBlank;
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
    private String username;

    @NotBlank(message = "Password must not be blank")
    private String password;
}
