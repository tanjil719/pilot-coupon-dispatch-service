package com.pilotcoupondispatchservice.modules.vehicles.dto;

import com.pilotcoupondispatchservice.enums.VehicleType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class VehicleUpdateRequest {

    @NotBlank(message = "Name must not be blank")
    @Size(max = 100, message = "Name must not exceed 100 characters")
    private String name;

    @NotBlank(message = "Registration number must not be blank")
    @Size(max = 50, message = "Registration number must not exceed 50 characters")
    private String registrationNo;

    @NotNull(message = "Type must not be null")
    private VehicleType type;

    @NotNull(message = "Capacity must not be null")
    @Positive(message = "Capacity must be greater than 0")
    private Double capacity;

    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;
}
