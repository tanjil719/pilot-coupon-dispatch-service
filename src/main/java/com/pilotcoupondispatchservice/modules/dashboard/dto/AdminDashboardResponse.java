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
public class AdminDashboardResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    // Most important group: the review queue that drives the admin's day, so it comes first.
    private PendingActionsDto pendingActions = new PendingActionsDto();

    private AdminBookingStatsDto bookings = new AdminBookingStatsDto();
    private AdminCouponStatsDto coupons = new AdminCouponStatsDto();
    private PaymentStatsDto payments = new PaymentStatsDto();
    private ResourceStatsDto resources = new ResourceStatsDto();
    private List<RecentBookingAdminDto> recentBookings = new ArrayList<>();
    private List<RecentCouponRequestDto> recentCouponRequests = new ArrayList<>();
}
