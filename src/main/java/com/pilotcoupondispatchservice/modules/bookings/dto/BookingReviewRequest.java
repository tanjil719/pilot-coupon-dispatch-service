package com.pilotcoupondispatchservice.modules.bookings.dto;

import com.pilotcoupondispatchservice.enums.BookingStatus;
import jakarta.validation.constraints.NotNull;
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

    @NotNull(message = "Status must not be null")
    private BookingStatus status;

    @Size(max = 500, message = "Reason must not exceed 500 characters")
    private String reason;

    private Long pilotId;
}
