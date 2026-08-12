package com.pilotcoupondispatchservice.modules.bookings.dto;

import com.pilotcoupondispatchservice.enums.BookingStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BookingReviewRequest {

    @NotBlank(message = "Status must not be blank")
    private BookingStatus status;

    @Size(max = 500, message = "Reason must not exceed 500 characters")
    private String reason;

    // TODO(next-module): once the Pilot module exists, this is the admin-selected pilot to assign
    // in the same transaction as approval (see BookingServiceImpl#reviewBooking).
    private Long pilotId;
}
