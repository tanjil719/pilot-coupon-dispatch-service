package com.pilotcoupondispatchservice.modules.coupons.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.pilotcoupondispatchservice.enums.CouponRequestStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CouponRequestResponse implements Serializable {

    private Long id;

    private String routeCode;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime serviceStart;

    private Double requestedAmount;

    private CouponRequestStatus status;
    private String rejectionReason;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime createdAt;
}
