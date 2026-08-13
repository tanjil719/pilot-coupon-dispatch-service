package com.pilotcoupondispatchservice.modules.bookings.controller;

import com.pilotcoupondispatchservice.enums.BookingStatus;
import com.pilotcoupondispatchservice.modules.bookings.dto.BookingAdminResponse;
import com.pilotcoupondispatchservice.modules.bookings.dto.BookingCancelRequest;
import com.pilotcoupondispatchservice.modules.bookings.dto.BookingReviewRequest;
import com.pilotcoupondispatchservice.modules.bookings.service.BookingService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("${url.base}/secured/admin/bookings")
@AllArgsConstructor
public class AdminBookingController {

    private final BookingService bookingService;

    @GetMapping
    public ResponseEntity<?> listAllBookings(
            @RequestParam(required = false) BookingStatus status,
            @RequestParam(required = false) String ownerName,
            @RequestParam(required = false) String routeCode,
            @RequestParam(required = false) @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss") LocalDateTime from,
            @RequestParam(required = false) @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss") LocalDateTime to,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") Sort.Direction sortDir) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDir, sortBy));
        Page<BookingAdminResponse> bookings = bookingService.listAllBookings(status, ownerName, routeCode, from, to, pageable);
        return ResponseEntity.ok(bookings);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBooking(@PathVariable Long id) {
        BookingAdminResponse response = bookingService.getBooking(id);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/review/{id}")
    public ResponseEntity<?> reviewBooking(@PathVariable Long id, @Valid @RequestBody BookingReviewRequest request) {
        BookingAdminResponse response = bookingService.reviewBooking(id, request);
        return ResponseEntity.ok(response);
    }
}
