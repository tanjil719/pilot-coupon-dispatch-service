package com.pilotcoupondispatchservice.modules.dashboard.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.pilotcoupondispatchservice.jackson.MoneySerializer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

// ACTIVE mirrors the persisted NOT_USED status; EXPIRED is derived at read-time by expiresAt, the
// same rule the rest of the app uses (see CouponRepository#ownerCouponStats).
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OwnerCouponStatsDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private long activeCount;

    @JsonSerialize(using = MoneySerializer.class)
    private BigDecimal activeTotalAmount;

    private long reservedCount;
    private long usedCount;
    private long expiredCount;
    private long expiringSoonCount;
}
