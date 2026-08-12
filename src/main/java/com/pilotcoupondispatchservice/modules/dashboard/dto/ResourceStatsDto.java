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
public class ResourceStatsDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private long totalOwners;
    private long totalVehicles;
    private long approvedVehicles;
    private long totalRoutes;
    private long activeRoutes;
    private long totalPilots;
    private long availablePilots;
    private long busyPilotsNow;
}
