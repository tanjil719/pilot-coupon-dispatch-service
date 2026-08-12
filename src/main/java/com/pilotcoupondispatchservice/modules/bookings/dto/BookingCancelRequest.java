package com.pilotcoupondispatchservice.modules.bookings.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Reason is optional here at the DTO level (the owner may cancel without one); the admin
// cancellation endpoint enforces its own NotBlank check in BookingServiceImpl since Jakarta
// validation on a shared DTO cannot vary strictness per caller.
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BookingCancelRequest {

    @Size(max = 500, message = "Reason must not exceed 500 characters")
    private String reason;
}
