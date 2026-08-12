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
public class PaymentStatsDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonSerialize(using = MoneySerializer.class)
    private BigDecimal totalCollectedAmount;

    @JsonSerialize(using = MoneySerializer.class)
    private BigDecimal totalForfeitedAmount;
}
