package com.pilotcoupondispatchservice.modules.bookings.service;

import com.pilotcoupondispatchservice.annotations.HasPermission;
import com.pilotcoupondispatchservice.constants.PermissionConstant;
import com.pilotcoupondispatchservice.enums.BookingStatus;
import com.pilotcoupondispatchservice.enums.CouponStatus;
import com.pilotcoupondispatchservice.enums.PaymentStatus;
import com.pilotcoupondispatchservice.enums.VehicleStatus;
import com.pilotcoupondispatchservice.exceptions.InvalidRequestException;
import com.pilotcoupondispatchservice.exceptions.ResourceNotFoundException;
import com.pilotcoupondispatchservice.modules.bookings.dto.*;
import com.pilotcoupondispatchservice.modules.bookings.entity.Booking;
import com.pilotcoupondispatchservice.modules.bookings.mapper.BookingMapper;
import com.pilotcoupondispatchservice.modules.bookings.repository.BookingRepository;
import com.pilotcoupondispatchservice.modules.bookings.repository.BookingSpecifications;
import com.pilotcoupondispatchservice.modules.coupons.entity.Coupon;
import com.pilotcoupondispatchservice.modules.coupons.repository.CouponRepository;
import com.pilotcoupondispatchservice.modules.coupons.service.CouponService;
import com.pilotcoupondispatchservice.modules.pilots.entity.Pilot;
import com.pilotcoupondispatchservice.modules.pilots.entity.PilotSchedule;
import com.pilotcoupondispatchservice.modules.pilots.service.PilotScheduleService;
import com.pilotcoupondispatchservice.modules.routes.entity.Route;
import com.pilotcoupondispatchservice.modules.routes.repository.RouteRepository;
import com.pilotcoupondispatchservice.modules.users.entity.User;
import com.pilotcoupondispatchservice.modules.users.repository.UserRepository;
import com.pilotcoupondispatchservice.modules.vehicles.entity.Vehicle;
import com.pilotcoupondispatchservice.modules.vehicles.repository.VehicleRepository;
import com.pilotcoupondispatchservice.utils.SecurityUtil;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.data.jpa.domain.Specification.where;

@Service
@AllArgsConstructor
public class BookingServiceImpl implements BookingService {

    private static final int DEFAULT_DURATION_MINUTES = 120;

    private final BookingRepository bookingRepository;
    private final VehicleRepository vehicleRepository;
    private final RouteRepository routeRepository;
    private final CouponRepository couponRepository;
    private final UserRepository userRepository;
    private final CouponService couponService;
    private final PilotScheduleService pilotScheduleService;

    @Override
    @HasPermission(permission = PermissionConstant.BOOKING_CREATE)
    @Transactional
    public BookingResponse createBooking(BookingCreateRequest request) {
        Long ownerId = SecurityUtil.getLoggedInUserId();

        User owner = userRepository.findByIdAndIsActiveTrue(ownerId).orElseThrow(() -> new ResourceNotFoundException("Owner not found with ID: " + ownerId));

        Vehicle vehicle = vehicleRepository.findByIdAndOwnerId(request.getVehicleId(), ownerId)
                .filter(v -> Boolean.TRUE.equals(v.getActive()))
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found with id: '" + request.getVehicleId() + "'"));

        if (vehicle.getStatus() != VehicleStatus.APPROVED) {
            throw new InvalidRequestException("Vehicle " + vehicle.getRegistrationNo() + " is not approved");
        }

        Route route = routeRepository.findByRouteCodeIgnoreCase(request.getRouteCode().trim())
                .orElseThrow(() -> new ResourceNotFoundException("Route not found with route code: " + request.getRouteCode()));

        if (!route.getActive()) {
            throw new InvalidRequestException("Route " + route.getRouteCode() + " is not active and cannot accept new bookings");
        }

        LocalDateTime serviceStart = request.getServiceStart();
        if (!serviceStart.isAfter(LocalDateTime.now())) {
            throw new InvalidRequestException("Service start must be in the future");
        }

        int durationMinutes = route.getEstimatedDurationMinutes() != null ? route.getEstimatedDurationMinutes() : DEFAULT_DURATION_MINUTES;
        LocalDateTime serviceEnd = serviceStart.plusMinutes(durationMinutes);

        if (bookingRepository.existsOverlappingBooking(vehicle.getId(), List.of(BookingStatus.APPROVED, BookingStatus.PENDING_APPROVAL), serviceStart, serviceEnd)) {
            throw new InvalidRequestException("Vehicle " + vehicle.getRegistrationNo() + " already has a booking overlapping this time window");
        }

        Coupon coupon = couponRepository.findByCodeAndOwnerId(request.getCouponCode().trim(), ownerId)
                .orElseThrow(() -> new ResourceNotFoundException("Coupon not found with code: '" + request.getCouponCode() + "'"));

        if (coupon.getStatus() != CouponStatus.NOT_USED) {
            throw new InvalidRequestException("Coupon " + coupon.getCode() + " is " + coupon.getStatus() + " and not available for booking");
        }

        if (!coupon.getExpiresAt().isAfter(LocalDateTime.now())) {
            throw new InvalidRequestException("Coupon " + coupon.getCode() + " expired at " + coupon.getExpiresAt());
        }

        if (coupon.getAmount().compareTo(route.getServiceFee()) < 0) {
            throw new InvalidRequestException("Coupon amount " + coupon.getAmount() + " is less than the required fee " + route.getServiceFee());
        }

        Booking booking = new Booking();
        booking.setOwner(owner);
        booking.setVehicle(vehicle);
        booking.setRoute(route);
        booking.setCoupon(coupon);
        booking.setServiceStart(serviceStart);
        booking.setServiceEnd(serviceEnd);
        booking.setStatus(BookingStatus.PENDING_APPROVAL);
        booking.setPaymentStatus(PaymentStatus.PAID);
        booking.setNote(request.getNote());
        Booking savedBooking = bookingRepository.save(booking);

        coupon.setStatus(CouponStatus.RESERVED);
        couponRepository.save(coupon);

        return BookingMapper.toResponse(savedBooking);
    }

    @Override
    @HasPermission(permission = PermissionConstant.BOOKING_VIEW_OWN)
    public Page<BookingResponse> listOwnBookings(BookingStatus status, LocalDateTime from, LocalDateTime to, Long vehicleId, Pageable pageable) {
        Long ownerId = SecurityUtil.getLoggedInUserId();

        Specification<Booking> specification = where(BookingSpecifications.ownerIdEquals(ownerId))
                .and(BookingSpecifications.statusEquals(status))
                .and(BookingSpecifications.serviceStartFrom(from))
                .and(BookingSpecifications.serviceStartTo(to))
                .and(BookingSpecifications.vehicleIdEquals(vehicleId))
                .and(BookingSpecifications.fetchAssociations());

        return bookingRepository.findAll(specification, pageable).map(BookingMapper::toResponse);
    }

    @Override
    @HasPermission(permission = PermissionConstant.BOOKING_VIEW_OWN)
    @Transactional(readOnly = true)
    public BookingDetailResponse getOwnBooking(Long id) {
        Long ownerId = SecurityUtil.getLoggedInUserId();
        Booking booking = findByIdAndOwnerIdOrThrow(id, ownerId);
        return BookingMapper.toDetailResponse(booking);
    }

    @Override
    @HasPermission(permission = PermissionConstant.BOOKING_CANCEL)
    @Transactional
    public BookingResponse cancelOwnBooking(Long id, BookingCancelRequest request) {
        Long ownerId = SecurityUtil.getLoggedInUserId();
        Booking booking = findByIdAndOwnerIdOrThrow(id, ownerId);

        if (booking.getStatus() != BookingStatus.PENDING_APPROVAL) {
            throw new InvalidRequestException("Only PENDING booking can be cancelled");
        }

        releaseCouponToActive(booking.getCoupon());

        booking.setStatus(BookingStatus.CANCELLED);
        booking.setPaymentStatus(PaymentStatus.UNPAID);
        booking.setCancellationReason(request.getReason());
        bookingRepository.save(booking);

        return BookingMapper.toResponse(booking);
    }

    @Override
    @HasPermission(permission = PermissionConstant.BOOKING_VIEW_ALL)
    @Transactional(readOnly = true)
    public Page<BookingAdminResponse> listAllBookings(BookingStatus status, Long ownerId, Long routeId, Long vehicleId, LocalDateTime from, LocalDateTime to, Pageable pageable) {
        // Default to PENDING_APPROVAL when no status filter is given, so the review queue is the
        // landing view.
        BookingStatus effectiveStatus = status == null ? BookingStatus.PENDING_APPROVAL : status;

        Specification<Booking> specification = where(BookingSpecifications.statusEquals(effectiveStatus))
                .and(BookingSpecifications.ownerIdEquals(ownerId))
                .and(BookingSpecifications.routeIdEquals(routeId))
                .and(BookingSpecifications.vehicleIdEquals(vehicleId))
                .and(BookingSpecifications.serviceStartFrom(from))
                .and(BookingSpecifications.serviceStartTo(to))
                .and(BookingSpecifications.fetchAssociations());

        return bookingRepository.findAll(specification, pageable)
                .map(booking -> BookingMapper.toAdminResponse(booking, booking.getRoute().getServiceFee()));
    }

    @Override
    @HasPermission(permission = PermissionConstant.BOOKING_VIEW_ALL)
    @Transactional(readOnly = true)
    public BookingAdminResponse getBooking(Long id) {
        Booking booking = findByIdOrThrow(id);
        return toAdminResponse(booking);
    }

    @Override
    @HasPermission(permission = PermissionConstant.BOOKING_STATUS_UPDATE)
    @Transactional
    public BookingAdminResponse reviewBooking(Long id, BookingReviewRequest request) {
        BookingStatus targetStatus = request.getStatus();

        if (targetStatus != BookingStatus.APPROVED && targetStatus != BookingStatus.REJECTED) {
            throw new InvalidRequestException("Status must be either APPROVED or REJECTED");
        }

        if (targetStatus == BookingStatus.APPROVED) {
            if (request.getPilotId() == null) {
                throw new InvalidRequestException("A pilot must be selected to approve a booking");
            }
            return approveBooking(id, request.getPilotId());
        }

        if (request.getReason() == null || request.getReason().trim().isEmpty()) {
            throw new InvalidRequestException("Reason is required to reject a booking");
        }

        return rejectBooking(id, request.getReason());
    }

    private BookingAdminResponse approveBooking(Long id, Long pilotId) {
        Booking booking = findByIdOrThrow(id);
        // assertTransition(booking.getStatus(), BookingStatus.APPROVED);

        if (booking.getStatus() != BookingStatus.PENDING_APPROVAL) {
            throw new InvalidRequestException("Only PENDING booking can be approved");
        }

        Coupon coupon = booking.getCoupon();

        if (coupon == null || coupon.getStatus() != CouponStatus.RESERVED || !coupon.getOwner().getId().equals(booking.getOwner().getId())) {
            throw new InvalidRequestException("Coupon reserved for this booking is no longer valid for approval");
        }

        if (!coupon.getExpiresAt().isAfter(LocalDateTime.now())) {
            throw new InvalidRequestException("Coupon " + coupon.getCode() + " expired while awaiting approval, reject this booking instead");
        }


        PilotSchedule schedule = pilotScheduleService.assignPilotToBooking(pilotId, booking);
        Pilot pilot = schedule.getPilot();
        booking.setPilot(pilot);
        booking.setAssignedAt(schedule.getAssignedAt());
        booking.setAssignedBy(SecurityUtil.getLoggedInUser().getName());

        coupon.setStatus(CouponStatus.USED);
        coupon.setUsedAt(LocalDateTime.now());
        couponRepository.save(coupon);

        booking.setStatus(BookingStatus.APPROVED);
        booking.setPaymentStatus(PaymentStatus.PAID);
        bookingRepository.save(booking);

        return toAdminResponse(booking);
    }

    private BookingAdminResponse rejectBooking(Long id, String reason) {
        Booking booking = findByIdOrThrow(id);

        if (booking.getStatus() != BookingStatus.PENDING_APPROVAL) {
            throw new InvalidRequestException("Only PENDING booking can be rejected");
        }

        releaseCouponToActive(booking.getCoupon());

        booking.setStatus(BookingStatus.REJECTED);
        booking.setPaymentStatus(PaymentStatus.UNPAID);
        booking.setRejectionReason(reason);
        bookingRepository.save(booking);

        return toAdminResponse(booking);
    }


    private void releaseCouponToActive(Coupon coupon) {
        if (coupon == null) {
            return;
        }

        coupon.setStatus(CouponStatus.NOT_USED);
        couponRepository.save(coupon);
    }

    private BookingAdminResponse toAdminResponse(Booking booking) {
        return BookingMapper.toAdminResponse(booking, booking.getRoute().getServiceFee());
    }

    private Booking findByIdOrThrow(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + id));
    }

    private Booking findByIdAndOwnerIdOrThrow(Long id, Long ownerId) {
        return bookingRepository.findByIdAndOwnerId(id, ownerId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + id));
    }
}
