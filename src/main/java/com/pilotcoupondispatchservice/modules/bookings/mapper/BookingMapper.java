package com.pilotcoupondispatchservice.modules.bookings.mapper;

import com.pilotcoupondispatchservice.modules.bookings.dto.BookingAdminResponse;
import com.pilotcoupondispatchservice.modules.bookings.dto.BookingDetailResponse;
import com.pilotcoupondispatchservice.modules.bookings.dto.BookingResponse;
import com.pilotcoupondispatchservice.modules.bookings.entity.Booking;
import com.pilotcoupondispatchservice.modules.coupons.entity.Coupon;
import com.pilotcoupondispatchservice.modules.pilots.entity.Pilot;
import com.pilotcoupondispatchservice.modules.routes.entity.Route;
import com.pilotcoupondispatchservice.modules.users.entity.User;
import com.pilotcoupondispatchservice.modules.vehicles.entity.Vehicle;

public final class BookingMapper {

    private BookingMapper() {
    }

    public static BookingResponse toResponse(Booking booking) {
        BookingResponse response = new BookingResponse();
        response.setId(booking.getId());

        Vehicle vehicle = booking.getVehicle();
        response.setVehicleName(vehicle.getName());
        response.setVehicleRegistrationNo(vehicle.getRegistrationNo());

        Route route = booking.getRoute();
        response.setRouteCode(route.getRouteCode());
        response.setRouteName(route.getName());
        response.setStartPoint(route.getStartPoint());
        response.setEndPoint(route.getEndPoint());

        response.setServiceStart(booking.getServiceStart());
        response.setApproximateServiceEnd(booking.getServiceEnd());

        Coupon coupon = booking.getCoupon();
        if (coupon != null) {
            response.setCouponCode(coupon.getCode());
            response.setCouponAmount(coupon.getAmount());
        }

        Pilot pilot = booking.getPilot();
        if (pilot != null) {
            response.setPilotName(pilot.getName());
            response.setPilotPhone(pilot.getPhone());
        }

        response.setStatus(booking.getStatus());
        response.setPaymentStatus(booking.getPaymentStatus());
        response.setRejectionReason(booking.getRejectionReason());
        response.setCancellationReason(booking.getCancellationReason());
        response.setCreatedAt(booking.getCreatedAt());
        return response;
    }

    public static BookingDetailResponse toDetailResponse(Booking booking) {
        BookingDetailResponse response = new BookingDetailResponse();
        BookingResponse base = toResponse(booking);

        response.setId(base.getId());
        response.setVehicleName(base.getVehicleName());
        response.setVehicleRegistrationNo(base.getVehicleRegistrationNo());
        response.setRouteCode(base.getRouteCode());
        response.setRouteName(base.getRouteName());
        response.setStartPoint(base.getStartPoint());
        response.setEndPoint(base.getEndPoint());
        response.setServiceStart(base.getServiceStart());
        response.setServiceEnd(base.getApproximateServiceEnd());
        response.setCouponCode(base.getCouponCode());
        response.setCouponAmount(base.getCouponAmount());
        response.setPilotName(base.getPilotName());
        response.setPilotPhone(base.getPilotPhone());
        response.setStatus(base.getStatus());
        response.setPaymentStatus(base.getPaymentStatus());
        response.setRejectionReason(base.getRejectionReason());
        response.setCancellationReason(base.getCancellationReason());
        response.setCreatedAt(base.getCreatedAt());

        return response;
    }

    public static BookingAdminResponse toAdminResponse(Booking booking, Double currentRouteFee) {
        BookingAdminResponse response = new BookingAdminResponse();
        response.setId(booking.getId());

        User owner = booking.getOwner();
        response.setOwnerId(owner.getId());
        response.setOwnerName(owner.getName());
        response.setOwnerEmail(owner.getEmail());
        response.setOwnerPhone(owner.getPhone());

        Vehicle vehicle = booking.getVehicle();
        response.setVehicleName(vehicle.getName());
        response.setVehicleRegistrationNo(vehicle.getRegistrationNo());

        Route route = booking.getRoute();
        response.setRouteCode(route.getRouteCode());
        response.setRouteName(route.getName());
        response.setStartPoint(route.getStartPoint());
        response.setEndPoint(route.getEndPoint());

        response.setServiceStart(booking.getServiceStart());
        response.setServiceEnd(booking.getServiceEnd());
        response.setCurrentRouteFee(currentRouteFee);

        Coupon coupon = booking.getCoupon();
        if (coupon != null) {
            response.setCouponCode(coupon.getCode());
            response.setCouponAmount(coupon.getAmount());
        }

        Pilot pilot = booking.getPilot();
        if (pilot != null) {
            response.setPilotCode(pilot.getPilotCode());
            response.setPilotName(pilot.getName());
            response.setPilotPhone(pilot.getPhone());
        }

        response.setStatus(booking.getStatus());
        response.setPaymentStatus(booking.getPaymentStatus());
        response.setRejectionReason(booking.getRejectionReason());
        response.setCancellationReason(booking.getCancellationReason());

        response.setCreatedAt(booking.getCreatedAt());

        return response;
    }
}
