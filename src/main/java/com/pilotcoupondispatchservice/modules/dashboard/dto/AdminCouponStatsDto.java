package com.pilotcoupondispatchservice.modules.dashboard.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.pilotcoupondispatchservice.jackson.MoneySerializer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AdminCouponStatsDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private long issuedCount;

    @JsonSerialize(using = MoneySerializer.class)
    private BigDecimal issuedTotalAmount;

    private long activeCount;

    @JsonSerialize(using = MoneySerializer.class)
    private BigDecimal activeTotalAmount;

    private long reservedCount;
    private long usedCount;

    @JsonSerialize(using = MoneySerializer.class)
    private BigDecimal usedTotalAmount;

    private long expiredCount;
}
