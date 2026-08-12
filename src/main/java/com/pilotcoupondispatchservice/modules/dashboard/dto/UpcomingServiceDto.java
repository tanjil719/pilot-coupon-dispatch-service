package com.pilotcoupondispatchservice.modules.dashboard.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.pilotcoupondispatchservice.enums.BookingStatus;
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
public class UpcomingServiceDto implements Serializable {

    private static final long serialVersionUID = 1L;

    // No dedicated booking-number column exists yet; this is the booking's id.
    private Long bookingNo;

    private String vehicleName;
    private String routeCode;
    private String startPoint;
    private String endPoint;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime serviceStart;

    private BookingStatus status;

    private String pilotName;
    private String pilotPhone;
}
