package com.pilotcoupondispatchservice.modules.bookings.service;

import com.pilotcoupondispatchservice.enums.BookingStatus;
import com.pilotcoupondispatchservice.modules.bookings.dto.BookingAdminResponse;
import com.pilotcoupondispatchservice.modules.bookings.dto.BookingCancelRequest;
import com.pilotcoupondispatchservice.modules.bookings.dto.BookingCreateRequest;
import com.pilotcoupondispatchservice.modules.bookings.dto.BookingDetailResponse;
import com.pilotcoupondispatchservice.modules.bookings.dto.BookingResponse;
import com.pilotcoupondispatchservice.modules.bookings.dto.BookingReviewRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface BookingService {

    // ---- Owner ----

    BookingResponse createBooking(BookingCreateRequest request);

    Page<BookingResponse> listOwnBookings(BookingStatus status, LocalDateTime from, LocalDateTime to, String registrationNo, Pageable pageable);

    BookingDetailResponse getOwnBooking(Long id);

    BookingResponse cancelOwnBooking(Long id, BookingCancelRequest request);

    // ---- Admin ----

    Page<BookingAdminResponse> listAllBookings(BookingStatus status, String ownerName, String routeCode, LocalDateTime from, LocalDateTime to, Pageable pageable);

    BookingAdminResponse getBooking(Long id);

    // Single approve/reject decision; request.getStatus() must be APPROVED or REJECTED.
    BookingAdminResponse reviewBooking(Long id, BookingReviewRequest request);

//    BookingAdminResponse startBooking(Long id);
//
//    BookingAdminResponse completeBooking(Long id);
//
//    BookingAdminResponse cancelBooking(Long id, BookingCancelRequest request);
}
