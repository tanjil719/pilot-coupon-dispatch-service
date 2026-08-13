package com.pilotcoupondispatchservice.modules.pilots.mapper;

import com.pilotcoupondispatchservice.modules.bookings.entity.Booking;
import com.pilotcoupondispatchservice.modules.pilots.dto.PilotScheduleResponse;
import com.pilotcoupondispatchservice.modules.pilots.entity.PilotSchedule;

public final class PilotScheduleMapper {

    private PilotScheduleMapper() {
    }

    public static PilotScheduleResponse toResponse(PilotSchedule schedule) {
        PilotScheduleResponse response = new PilotScheduleResponse();
        response.setId(schedule.getId());

        Booking booking = schedule.getBooking();
        response.setBookingId(booking.getId());
//        response.setVehicleRegistrationNo(booking.getVehicle().getRegistrationNo());

        response.setPilotName(schedule.getPilot().getName());
        response.setRouteCode(schedule.getRouteCode());
        response.setServiceStart(schedule.getServiceStart());
        response.setServiceEnd(schedule.getServiceEnd());
        response.setStatus(schedule.getBooking().getStatus());
        response.setAssignedAt(schedule.getAssignedAt());
        return response;
    }
}
