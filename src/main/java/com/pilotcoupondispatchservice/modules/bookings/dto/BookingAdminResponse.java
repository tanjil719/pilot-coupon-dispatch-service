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

// Admin-facing detail view: full booking detail plus owner identity and the route's current live fee.
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BookingAdminResponse implements Serializable {

    private Long id;

    private Long ownerId;
    private String ownerName;
    private String ownerEmail;
    private String ownerPhone;

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

    private Double currentRouteFee;

    private String couponCode;
    private Double couponAmount;

    private BookingStatus status;
    private PaymentStatus paymentStatus;

    private String rejectionReason;
    private String cancellationReason;

    private String reviewedByName;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime reviewedAt;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime createdAt;

}
