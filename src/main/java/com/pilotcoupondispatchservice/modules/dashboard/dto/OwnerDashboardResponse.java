package com.pilotcoupondispatchservice.modules.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OwnerDashboardResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private VehicleStatsDto vehicles = new VehicleStatsDto();
    private BookingStatsDto bookings = new BookingStatsDto();
    private OwnerCouponStatsDto coupons = new OwnerCouponStatsDto();
    private CouponRequestStatsDto couponRequests = new CouponRequestStatsDto();
    private List<ActionItemDto> actionRequired = new ArrayList<>();
    private List<UpcomingServiceDto> upcomingServices = new ArrayList<>();
    private List<RecentBookingDto> recentBookings = new ArrayList<>();
}
