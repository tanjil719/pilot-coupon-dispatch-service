package com.pilotcoupondispatchservice.modules.dashboard.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.pilotcoupondispatchservice.jackson.MoneySerializer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RecentCouponRequestDto implements Serializable {

    private static final long serialVersionUID = 1L;

    // No dedicated request-number column exists yet; this is the request's id.
    private Long requestNo;

    private String ownerName;
    private String routeCode;

    @JsonSerialize(using = MoneySerializer.class)
    private BigDecimal requestedAmount;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime createdAt;
}
