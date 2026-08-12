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
public class CouponRequestStatsDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private long pending;
    private long approved;
    private long rejected;
}
