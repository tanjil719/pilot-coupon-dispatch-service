package com.pilotcoupondispatchservice.modules.routes.dto;

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
public class RouteRequest {

    @NotBlank(message = "Route code must not be blank")
    @Size(max = 30, message = "Route code must not exceed 30 characters")
    private String routeCode;

    @NotBlank(message = "Name must not be blank")
    @Size(max = 120, message = "Name must not exceed 120 characters")
    private String name;

    @NotBlank(message = "Start point must not be blank")
    @Size(max = 120, message = "Start point must not exceed 120 characters")
    private String startPoint;

    @NotBlank(message = "End point must not be blank")
    @Size(max = 120, message = "End point must not exceed 120 characters")
    private String endPoint;

    @NotNull(message = "Distance must not be null")
    @Positive(message = "Distance must be greater than 0")
    private Double distanceKm;

    @Positive(message = "Estimated duration must be greater than 0")
    private Integer estimatedDurationMinutes;

    @NotNull(message = "Service fee must not be null")
    @Positive(message = "Service fee must be greater than 0")
    private Double serviceFee;

    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;
}
