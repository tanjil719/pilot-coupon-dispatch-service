package com.pilotcoupondispatchservice.modules.vehicles.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.pilotcoupondispatchservice.enums.VehicleStatus;
import com.pilotcoupondispatchservice.enums.VehicleType;
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
public class VehicleResponse implements Serializable {

    private Long id;
    private String name;
    private String registrationNo;
    private VehicleType type;
    private Double capacity;
    private String description;
    private VehicleStatus status;
    private String rejectionReason;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime updatedAt;
//
//    private boolean editable;
//    private boolean bookable;
}
