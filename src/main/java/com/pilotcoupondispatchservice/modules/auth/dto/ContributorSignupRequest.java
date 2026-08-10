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
public class ContributorSignupRequest {

    @NotBlank(message = "Name must not be blank")
    private String name;

    @NotBlank(message = "Email must not be blank")
    private String email;

    @NotBlank(message = "Phone must not be blank")
    private String phone;

    @NotBlank(message = "Password must not be blank")
    private String password;
}
