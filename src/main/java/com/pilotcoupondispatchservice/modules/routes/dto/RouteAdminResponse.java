package com.pilotcoupondispatchservice.modules.routes.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RouteAdminResponse implements Serializable {

    private Long id;
    private String routeCode;
    private String name;
    private String startPoint;
    private String endPoint;
    private Double distanceKm;
    private Integer estimatedDurationMinutes;

    private Double serviceFee;

    private String description;

    private Boolean active;
    private long createdByName;
    private long updatedByName;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime updatedAt;
}
