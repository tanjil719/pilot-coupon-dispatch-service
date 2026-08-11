package com.pilotcoupondispatchservice.modules.vehicles.dto;

import com.pilotcoupondispatchservice.enums.VehicleStatus;
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
public class VehicleReviewRequest {

    @NotBlank(message = "Status must not be null")
    private VehicleStatus status;

    @Size(max = 500, message = "Rejection reason must not exceed 500 characters")
    private String reason;
}
