package com.pilotcoupondispatchservice.modules.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class VehicleStatsDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private long total;
    private long pending;
    private long approved;
    private long rejected;
}
