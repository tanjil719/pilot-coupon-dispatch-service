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
public class BookingStatsDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private long total;
    private long pendingApproval;
    private long approved;
    private long inProgress;
    private long completed;
    private long rejected;
    private long cancelled;
}
