package com.pilotcoupondispatchservice.modules.pilots.mapper;

import com.pilotcoupondispatchservice.modules.pilots.dto.AvailablePilotResponse;
import com.pilotcoupondispatchservice.modules.pilots.dto.PilotDetailResponse;
import com.pilotcoupondispatchservice.modules.pilots.dto.PilotNextScheduleResponse;
import com.pilotcoupondispatchservice.modules.pilots.dto.PilotResponse;
import com.pilotcoupondispatchservice.modules.pilots.entity.Pilot;
import com.pilotcoupondispatchservice.modules.pilots.entity.PilotSchedule;

public final class PilotMapper {

    private PilotMapper() {
    }

    public static PilotResponse toResponse(Pilot pilot) {
        PilotResponse response = new PilotResponse();
        response.setId(pilot.getId());
        response.setPilotCode(pilot.getPilotCode());
        response.setName(pilot.getName());
        response.setLicenseNo(pilot.getLicenseNo());
        response.setPhone(pilot.getPhone());
        response.setExperienceYears(pilot.getExperienceYears());
        response.setStatus(pilot.getStatus());
        response.setCreatedAt(pilot.getCreatedAt());
        return response;
    }

    public static PilotDetailResponse toDetailResponse(Pilot pilot, long scheduledCount, long inProgressCount, long completedCount, PilotSchedule nextSchedule) {
        PilotDetailResponse response = new PilotDetailResponse();
        response.setId(pilot.getId());
        response.setPilotCode(pilot.getPilotCode());
        response.setName(pilot.getName());
        response.setLicenseNo(pilot.getLicenseNo());
        response.setPhone(pilot.getPhone());
        response.setExperienceYears(pilot.getExperienceYears());
        response.setStatus(pilot.getStatus());
        response.setCreatedAt(pilot.getCreatedAt());

        response.setScheduledCount(scheduledCount);
        response.setInProgressCount(inProgressCount);
        response.setCompletedCount(completedCount);

        if (nextSchedule != null) {
            PilotNextScheduleResponse next = new PilotNextScheduleResponse();
            next.setBookingId(nextSchedule.getBooking().getId());
            next.setRouteCode(nextSchedule.getRouteCode());
            next.setServiceStart(nextSchedule.getServiceStart());
            next.setServiceEnd(nextSchedule.getServiceEnd());
            response.setNextSchedule(next);
        }

        return response;
    }

    public static AvailablePilotResponse toAvailableResponse(Pilot pilot, long completedCount) {
        AvailablePilotResponse response = new AvailablePilotResponse();
        response.setId(pilot.getId());
        response.setPilotCode(pilot.getPilotCode());
        response.setName(pilot.getName());
        response.setPhone(pilot.getPhone());
        response.setExperienceYears(pilot.getExperienceYears());
        response.setCompletedCount(completedCount);
        return response;
    }
}
