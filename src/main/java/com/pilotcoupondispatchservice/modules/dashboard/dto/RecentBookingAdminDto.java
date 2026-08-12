package com.pilotcoupondispatchservice.modules.dashboard.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.pilotcoupondispatchservice.enums.BookingStatus;
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
public class RecentBookingAdminDto implements Serializable {

    private static final long serialVersionUID = 1L;

    // No dedicated booking-number column exists yet; this is the booking's id.
    private Long bookingNo;

    private String ownerName;
    private String vehicleName;
    private String routeCode;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime serviceStart;

    @JsonSerialize(using = MoneySerializer.class)
    private BigDecimal feeAmount;

    private BookingStatus status;
    private String pilotName;
}
