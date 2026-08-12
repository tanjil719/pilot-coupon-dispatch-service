package com.pilotcoupondispatchservice.modules.pilots.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PilotCreateRequest {

    @NotBlank(message = "Name must not be blank")
    @Size(max = 120, message = "Name must not exceed 120 characters")
    private String name;

    @NotBlank(message = "License number must not be blank")
    @Size(max = 50, message = "License number must not exceed 50 characters")
    private String licenseNo;

    @NotBlank(message = "Phone must not be blank")
    @Size(max = 20, message = "Phone must not exceed 20 characters")
    private String phone;

    @PositiveOrZero(message = "Experience years must be zero or greater")
    private Integer experienceYears;
}
