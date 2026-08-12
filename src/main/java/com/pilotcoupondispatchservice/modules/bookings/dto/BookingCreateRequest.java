package com.pilotcoupondispatchservice.modules.bookings.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BookingCreateRequest {

    @NotNull(message = "Vehicle id must not be null")
    private Long vehicleId;

    @NotBlank(message = "Route code must not be blank")
    @Size(max = 30, message = "Route code must not exceed 30 characters")
    private String routeCode;

    @NotNull(message = "Service start must not be null")
    private LocalDateTime serviceStart;

    @NotBlank(message = "Coupon code must not be blank")
    @Size(max = 30, message = "Coupon code must not exceed 30 characters")
    private String couponCode;

    @Size(max = 500, message = "Note must not exceed 500 characters")
    private String note;
}
