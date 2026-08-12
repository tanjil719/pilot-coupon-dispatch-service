package com.pilotcoupondispatchservice.modules.pilots.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AvailablePilotResponse implements Serializable {

    private Long id;
    private String pilotCode;
    private String name;
    private String phone;
    private Integer experienceYears;
    private long completedCount;
}
