package com.pilotcoupondispatchservice.modules.dashboard.service;

import com.pilotcoupondispatchservice.annotations.HasPermission;
import com.pilotcoupondispatchservice.constants.PermissionConstant;
import com.pilotcoupondispatchservice.enums.BookingStatus;
import com.pilotcoupondispatchservice.enums.CouponRequestStatus;
import com.pilotcoupondispatchservice.enums.PilotStatus;
import com.pilotcoupondispatchservice.enums.UserType;
import com.pilotcoupondispatchservice.enums.VehicleStatus;
import com.pilotcoupondispatchservice.modules.bookings.entity.Booking;
import com.pilotcoupondispatchservice.modules.bookings.repository.BookingRepository;
import com.pilotcoupondispatchservice.modules.coupons.entity.Coupon;
import com.pilotcoupondispatchservice.modules.coupons.entity.CouponRequest;
import com.pilotcoupondispatchservice.modules.coupons.repository.CouponRepository;
import com.pilotcoupondispatchservice.modules.coupons.repository.CouponRequestRepository;
import com.pilotcoupondispatchservice.modules.dashboard.dto.ActionItemDto;
import com.pilotcoupondispatchservice.modules.dashboard.dto.AdminBookingStatsDto;
import com.pilotcoupondispatchservice.modules.dashboard.dto.AdminCouponStatsDto;
import com.pilotcoupondispatchservice.modules.dashboard.dto.AdminDashboardResponse;
import com.pilotcoupondispatchservice.modules.dashboard.dto.BookingStatsDto;
import com.pilotcoupondispatchservice.modules.dashboard.dto.CouponRequestStatsDto;
import com.pilotcoupondispatchservice.modules.dashboard.dto.OwnerCouponStatsDto;
import com.pilotcoupondispatchservice.modules.dashboard.dto.OwnerDashboardResponse;
import com.pilotcoupondispatchservice.modules.dashboard.dto.PaymentStatsDto;
import com.pilotcoupondispatchservice.modules.dashboard.dto.PendingActionsDto;
import com.pilotcoupondispatchservice.modules.dashboard.dto.RecentBookingAdminDto;
import com.pilotcoupondispatchservice.modules.dashboard.dto.RecentBookingDto;
import com.pilotcoupondispatchservice.modules.dashboard.dto.RecentCouponRequestDto;
import com.pilotcoupondispatchservice.modules.dashboard.dto.ResourceStatsDto;
import com.pilotcoupondispatchservice.modules.dashboard.dto.UpcomingServiceDto;
import com.pilotcoupondispatchservice.modules.dashboard.dto.VehicleStatsDto;
import com.pilotcoupondispatchservice.modules.pilots.repository.PilotRepository;
import com.pilotcoupondispatchservice.modules.pilots.repository.PilotScheduleRepository;
import com.pilotcoupondispatchservice.modules.routes.repository.RouteRepository;
import com.pilotcoupondispatchservice.modules.users.repository.UserRepository;
import com.pilotcoupondispatchservice.modules.vehicles.entity.Vehicle;
import com.pilotcoupondispatchservice.modules.vehicles.repository.VehicleRepository;
import com.pilotcoupondispatchservice.utils.SecurityUtil;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private static final int RECENT_LIMIT = 5;
    private static final int ACTION_REQUIRED_LIMIT = 5;
    private static final int EXPIRING_SOON_DAYS = 7;

    private final VehicleRepository vehicleRepository;
    private final RouteRepository routeRepository;
    private final CouponRepository couponRepository;
    private final CouponRequestRepository couponRequestRepository;
    private final BookingRepository bookingRepository;
    private final PilotRepository pilotRepository;
    private final PilotScheduleRepository pilotScheduleRepository;
    private final UserRepository userRepository;

    @Override
    @HasPermission(permission = PermissionConstant.DASHBOARD_VIEW_OWN)
    @Transactional(readOnly = true)
    public OwnerDashboardResponse getOwnerDashboard() {
        Long ownerId = SecurityUtil.getLoggedInUserId();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime soon = now.plusDays(EXPIRING_SOON_DAYS);

        OwnerDashboardResponse response = new OwnerDashboardResponse();
        response.setVehicles(buildOwnerVehicleStats(ownerId));
        response.setBookings(buildOwnerBookingStats(ownerId));
        response.setCoupons(buildOwnerCouponStats(ownerId, now, soon));
        response.setCouponRequests(buildOwnerCouponRequestStats(ownerId));
        response.setActionRequired(buildActionRequired(ownerId, now, soon));
//        response.setUpcomingServices(buildUpcomingServices(ownerId, now));
        response.setRecentBookings(buildOwnerRecentBookings(ownerId));
        return response;
    }

    @Override
    @HasPermission(permission = PermissionConstant.DASHBOARD_VIEW_ALL)
    public AdminDashboardResponse getAdminDashboard() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime startOfNextDay = startOfDay.plusDays(1);

        Map<VehicleStatus, Long> vehicleCounts = toCountMap(vehicleRepository.countGroupedByStatus());
        Map<BookingStatus, Long> bookingCounts = toCountMap(bookingRepository.countGroupedByStatus());

        AdminDashboardResponse response = new AdminDashboardResponse();
        response.setPendingActions(buildPendingActions(vehicleCounts, bookingCounts));
        response.setBookings(buildAdminBookingStats(bookingCounts, startOfDay, startOfNextDay));
        response.setCoupons(buildAdminCouponStats(now));
//        response.setPayments(buildPaymentStats());
        response.setResources(buildResourceStats(vehicleCounts, now));
        response.setRecentBookings(buildAdminRecentBookings());
        response.setRecentCouponRequests(buildAdminRecentCouponRequests());
        return response;
    }

    //*********** Owner builders ***********//

    private VehicleStatsDto buildOwnerVehicleStats(Long ownerId) {
        Map<VehicleStatus, Long> counts = toCountMap(vehicleRepository.countGroupedByStatusForOwner(ownerId));

        VehicleStatsDto dto = new VehicleStatsDto();
        dto.setTotal(sum(counts));
        dto.setPending(counts.getOrDefault(VehicleStatus.PENDING, 0L));
        dto.setApproved(counts.getOrDefault(VehicleStatus.APPROVED, 0L));
        dto.setRejected(counts.getOrDefault(VehicleStatus.REJECTED, 0L));
        return dto;
    }

    private BookingStatsDto buildOwnerBookingStats(Long ownerId) {
        Map<BookingStatus, Long> counts = toCountMap(bookingRepository.countGroupedByStatusForOwner(ownerId));
        return toBookingStats(new BookingStatsDto(), counts);
    }

    private OwnerCouponStatsDto buildOwnerCouponStats(Long ownerId, LocalDateTime now, LocalDateTime soon) {
        Object[] row = couponRepository.ownerCouponStats(ownerId, now, soon);

        OwnerCouponStatsDto dto = new OwnerCouponStatsDto();
        dto.setActiveCount(toLong(row[0]));
        dto.setActiveTotalAmount(toMoney(row[1]));
        dto.setReservedCount(toLong(row[2]));
        dto.setUsedCount(toLong(row[3]));
        dto.setExpiredCount(toLong(row[4]));
        dto.setExpiringSoonCount(toLong(row[5]));
        return dto;
    }

    private CouponRequestStatsDto buildOwnerCouponRequestStats(Long ownerId) {
        Map<CouponRequestStatus, Long> counts = toCountMap(couponRequestRepository.countGroupedByStatusForOwner(ownerId));

        CouponRequestStatsDto dto = new CouponRequestStatsDto();
        dto.setPending(counts.getOrDefault(CouponRequestStatus.PENDING, 0L));
        dto.setApproved(counts.getOrDefault(CouponRequestStatus.APPROVED, 0L));
        dto.setRejected(counts.getOrDefault(CouponRequestStatus.REJECTED, 0L));
        return dto;
    }

    private List<ActionItemDto> buildActionRequired(Long ownerId, LocalDateTime now, LocalDateTime soon) {
        List<ActionItemDto> items = new ArrayList<>();

        for (Coupon coupon : couponRepository.findExpiringSoonForOwner(ownerId, now, soon, PageRequest.of(0, ACTION_REQUIRED_LIMIT))) {
            items.add(new ActionItemDto(
                    "COUPON_EXPIRING",
                    "Coupon " + coupon.getCode() + " expires on " + coupon.getExpiresAt(),
                    coupon.getId()));
        }

        for (CouponRequest request : couponRequestRepository.findApprovedWithoutBookingForOwner(ownerId, PageRequest.of(0, ACTION_REQUIRED_LIMIT))) {
            items.add(new ActionItemDto(
                    "COUPON_REQUEST_UNUSED",
                    "Approved coupon request for route " + request.getRouteCode() + " has no booking yet",
                    request.getId()));
        }

        for (Vehicle vehicle : vehicleRepository.findTop5ByOwnerIdAndStatusOrderByUpdatedAtDesc(ownerId, VehicleStatus.REJECTED)) {
            items.add(new ActionItemDto(
                    "VEHICLE_REJECTED",
                    "Vehicle " + vehicle.getName() + " was rejected: " + vehicle.getRejectionReason(),
                    vehicle.getId()));
        }

        return items.size() > ACTION_REQUIRED_LIMIT ? items.subList(0, ACTION_REQUIRED_LIMIT) : items;
    }

    private List<UpcomingServiceDto> buildUpcomingServices(Long ownerId, LocalDateTime now) {
        List<UpcomingServiceDto> result = new ArrayList<>();

        for (Booking booking : bookingRepository.findUpcomingForOwner(ownerId, now, PageRequest.of(0, RECENT_LIMIT))) {
            UpcomingServiceDto dto = new UpcomingServiceDto();
            dto.setBookingNo(booking.getId());
            dto.setVehicleName(booking.getVehicle().getName());
            dto.setRouteCode(booking.getRoute().getRouteCode());
            dto.setStartPoint(booking.getRoute().getStartPoint());
            dto.setEndPoint(booking.getRoute().getEndPoint());
            dto.setServiceStart(booking.getServiceStart());
            dto.setStatus(booking.getStatus());
            if (booking.getPilot() != null) {
                dto.setPilotName(booking.getPilot().getName());
                dto.setPilotPhone(booking.getPilot().getPhone());
            }
            result.add(dto);
        }

        return result;
    }

    private List<RecentBookingDto> buildOwnerRecentBookings(Long ownerId) {
        List<RecentBookingDto> result = new ArrayList<>();

        for (Booking booking : bookingRepository.findRecentForOwner(ownerId, PageRequest.of(0, RECENT_LIMIT))) {
            RecentBookingDto dto = new RecentBookingDto();
            dto.setBookingNo(booking.getId());
            dto.setRouteCode(booking.getRoute().getRouteCode());
            dto.setServiceStart(booking.getServiceStart());
            dto.setFeeAmount(BigDecimal.valueOf(booking.getRoute().getServiceFee()));
            dto.setStatus(booking.getStatus());
            dto.setPaymentStatus(booking.getPaymentStatus());
            result.add(dto);
        }

        return result;
    }

    //*********** Admin builders ***********//

    private PendingActionsDto buildPendingActions(Map<VehicleStatus, Long> vehicleCounts, Map<BookingStatus, Long> bookingCounts) {
        PendingActionsDto dto = new PendingActionsDto();
        dto.setVehiclesPendingApproval(vehicleCounts.getOrDefault(VehicleStatus.PENDING, 0L));
        dto.setCouponRequestsPending(couponRequestRepository.countByStatus(CouponRequestStatus.PENDING));
        dto.setBookingsPendingApproval(bookingCounts.getOrDefault(BookingStatus.PENDING_APPROVAL, 0L));
        return dto;
    }

    private AdminBookingStatsDto buildAdminBookingStats(Map<BookingStatus, Long> counts, LocalDateTime startOfDay, LocalDateTime startOfNextDay) {
        AdminBookingStatsDto dto = toBookingStats(new AdminBookingStatsDto(), counts);
        dto.setTodayServiceCount(bookingRepository.countByServiceStartBetween(startOfDay, startOfNextDay));
        return dto;
    }

    private AdminCouponStatsDto buildAdminCouponStats(LocalDateTime now) {
        Object[] row = couponRepository.adminCouponStats(now);

        AdminCouponStatsDto dto = new AdminCouponStatsDto();
        dto.setIssuedCount(toLong(row[0]));
        dto.setIssuedTotalAmount(toMoney(row[1]));
        dto.setActiveCount(toLong(row[2]));
        dto.setActiveTotalAmount(toMoney(row[3]));
        dto.setReservedCount(toLong(row[4]));
        dto.setUsedCount(toLong(row[5]));
        dto.setUsedTotalAmount(toMoney(row[6]));
        dto.setExpiredCount(toLong(row[7]));
        return dto;
    }

    private PaymentStatsDto buildPaymentStats() {
        Object[] row = bookingRepository.paymentTotals();

        PaymentStatsDto dto = new PaymentStatsDto();
        dto.setTotalCollectedAmount(toMoney(row[0]));
        dto.setTotalForfeitedAmount(toMoney(row[1]));
        return dto;
    }

    private ResourceStatsDto buildResourceStats(Map<VehicleStatus, Long> vehicleCounts, LocalDateTime now) {
        ResourceStatsDto dto = new ResourceStatsDto();
        dto.setTotalOwners(userRepository.countByUserType(UserType.OWNER));
        dto.setTotalVehicles(sum(vehicleCounts));
        dto.setApprovedVehicles(vehicleCounts.getOrDefault(VehicleStatus.APPROVED, 0L));
        dto.setTotalRoutes(routeRepository.count());
        dto.setActiveRoutes(routeRepository.countByActiveTrue());
        dto.setTotalPilots(pilotRepository.count());
        dto.setAvailablePilots(pilotRepository.countByStatus(PilotStatus.AVAILABLE));
        dto.setBusyPilotsNow(pilotScheduleRepository.countBusyPilotsAt(now));
        return dto;
    }

    private List<RecentBookingAdminDto> buildAdminRecentBookings() {
        List<RecentBookingAdminDto> result = new ArrayList<>();

        for (Booking booking : bookingRepository.findRecentForAdmin(PageRequest.of(0, RECENT_LIMIT))) {
            RecentBookingAdminDto dto = new RecentBookingAdminDto();
            dto.setBookingNo(booking.getId());
            dto.setOwnerName(booking.getOwner().getName());
            dto.setVehicleName(booking.getVehicle().getName());
            dto.setRouteCode(booking.getRoute().getRouteCode());
            dto.setServiceStart(booking.getServiceStart());
            dto.setFeeAmount(BigDecimal.valueOf(booking.getRoute().getServiceFee()));
            dto.setStatus(booking.getStatus());
            if (booking.getPilot() != null) {
                dto.setPilotName(booking.getPilot().getName());
            }
            result.add(dto);
        }

        return result;
    }

    private List<RecentCouponRequestDto> buildAdminRecentCouponRequests() {
        List<RecentCouponRequestDto> result = new ArrayList<>();

        for (CouponRequest request : couponRequestRepository.findRecentPending(PageRequest.of(0, RECENT_LIMIT))) {
            RecentCouponRequestDto dto = new RecentCouponRequestDto();
            dto.setRequestNo(request.getId());
            dto.setOwnerName(request.getOwner().getName());
            dto.setRouteCode(request.getRouteCode());
            dto.setRequestedAmount(BigDecimal.valueOf(request.getRequestedAmount()));
            dto.setCreatedAt(request.getCreatedAt());
            result.add(dto);
        }

        return result;
    }

    //*********** Internal Helper Methods ***********//

    private static <T extends BookingStatsDto> T toBookingStats(T dto, Map<BookingStatus, Long> counts) {
        dto.setTotal(sum(counts));
        dto.setPendingApproval(counts.getOrDefault(BookingStatus.PENDING_APPROVAL, 0L));
        dto.setApproved(counts.getOrDefault(BookingStatus.APPROVED, 0L));
        dto.setInProgress(counts.getOrDefault(BookingStatus.IN_PROGRESS, 0L));
        dto.setCompleted(counts.getOrDefault(BookingStatus.COMPLETED, 0L));
        dto.setRejected(counts.getOrDefault(BookingStatus.REJECTED, 0L));
        dto.setCancelled(counts.getOrDefault(BookingStatus.CANCELLED, 0L));
        return dto;
    }

    private static long sum(Map<?, Long> counts) {
        return counts.values().stream().mapToLong(Long::longValue).sum();
    }

    @SuppressWarnings("unchecked")
    private static <E> Map<E, Long> toCountMap(List<Object[]> rows) {
        Map<E, Long> map = new HashMap<>();
        for (Object[] row : rows) {
            map.put((E) row[0], (Long) row[1]);
        }
        return map;
    }

    private static long toLong(Object value) {
        return value == null ? 0L : ((Number) value).longValue();
    }

    private static BigDecimal toMoney(Object value) {
        BigDecimal amount = value == null ? BigDecimal.ZERO : BigDecimal.valueOf(((Number) value).doubleValue());
        return amount.setScale(2, RoundingMode.HALF_UP);
    }
}
