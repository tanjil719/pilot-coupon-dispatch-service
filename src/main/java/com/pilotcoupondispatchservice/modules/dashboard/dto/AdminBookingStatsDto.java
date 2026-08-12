package com.pilotcoupondispatchservice.modules.dashboard.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminBookingStatsDto extends BookingStatsDto {

    private static final long serialVersionUID = 1L;

    // Everything else (total, pendingApproval, approved, inProgress, completed, rejected,
    // cancelled) is inherited from BookingStatsDto so the two shapes cannot drift apart.
    private long todayServiceCount;
}
