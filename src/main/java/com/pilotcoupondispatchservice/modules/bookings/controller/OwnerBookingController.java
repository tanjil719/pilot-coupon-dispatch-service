package com.pilotcoupondispatchservice.modules.bookings.controller;

import com.pilotcoupondispatchservice.enums.BookingStatus;
import com.pilotcoupondispatchservice.modules.bookings.dto.BookingCancelRequest;
import com.pilotcoupondispatchservice.modules.bookings.dto.BookingCreateRequest;
import com.pilotcoupondispatchservice.modules.bookings.dto.BookingDetailResponse;
import com.pilotcoupondispatchservice.modules.bookings.dto.BookingResponse;
import com.pilotcoupondispatchservice.modules.bookings.service.BookingService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("${url.base}/secured/owner/bookings")
@AllArgsConstructor
public class OwnerBookingController {

    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<?> createBooking(@Valid @RequestBody BookingCreateRequest request) {
        BookingResponse response = bookingService.createBooking(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<?> listOwnBookings(
            @RequestParam(required = false) BookingStatus status,
            @RequestParam(required = false) @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss") LocalDateTime from,
            @RequestParam(required = false) @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss") LocalDateTime to,
            @RequestParam(required = false) Long vehicleId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") Sort.Direction sortDir) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDir, sortBy));
        Page<BookingResponse> bookings = bookingService.listOwnBookings(status, from, to, vehicleId, pageable);
        return ResponseEntity.ok(bookings);
    }

    @GetMapping("/by-id/{id}")
    public ResponseEntity<?> getOwnBooking(@PathVariable Long id) {
        BookingDetailResponse response = bookingService.getOwnBooking(id);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<?> cancelOwnBooking(@PathVariable Long id, @Valid @RequestBody BookingCancelRequest request) {
        BookingResponse response = bookingService.cancelOwnBooking(id, request);
        return ResponseEntity.ok(response);
    }
}
