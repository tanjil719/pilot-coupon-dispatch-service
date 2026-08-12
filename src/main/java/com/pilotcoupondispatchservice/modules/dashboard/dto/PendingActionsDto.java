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
public class PendingActionsDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private long vehiclesPendingApproval;
    private long couponRequestsPending;
    private long bookingsPendingApproval;
}
