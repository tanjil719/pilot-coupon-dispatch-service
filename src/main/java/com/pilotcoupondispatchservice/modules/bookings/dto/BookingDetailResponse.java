package com.pilotcoupondispatchservice.modules.bookings.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.pilotcoupondispatchservice.enums.BookingStatus;
import com.pilotcoupondispatchservice.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

// Owner-facing detail view.
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BookingDetailResponse implements Serializable {

    private Long id;

    private String vehicleName;
    private String vehicleRegistrationNo;

    private String routeCode;
    private String routeName;
    private String startPoint;
    private String endPoint;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime serviceStart;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime serviceEnd;

    private String couponCode;
    private Double couponAmount;

    private BookingStatus status;
    private PaymentStatus paymentStatus;

    private String rejectionReason;
    private String cancellationReason;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime completedAt;
}
