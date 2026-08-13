package com.pilotcoupondispatchservice.modules.pilots.service;

import com.pilotcoupondispatchservice.annotations.HasPermission;
import com.pilotcoupondispatchservice.constants.PermissionConstant;
import com.pilotcoupondispatchservice.enums.BookingStatus;
import com.pilotcoupondispatchservice.enums.PilotStatus;
import com.pilotcoupondispatchservice.enums.ScheduleStatus;
import com.pilotcoupondispatchservice.exceptions.InvalidRequestException;
import com.pilotcoupondispatchservice.exceptions.ResourceNotFoundException;
import com.pilotcoupondispatchservice.modules.bookings.entity.Booking;
import com.pilotcoupondispatchservice.modules.pilots.dto.PilotScheduleResponse;
import com.pilotcoupondispatchservice.modules.pilots.entity.Pilot;
import com.pilotcoupondispatchservice.modules.pilots.entity.PilotSchedule;
import com.pilotcoupondispatchservice.modules.pilots.mapper.PilotScheduleMapper;
import com.pilotcoupondispatchservice.modules.pilots.repository.PilotRepository;
import com.pilotcoupondispatchservice.modules.pilots.repository.PilotScheduleRepository;
import com.pilotcoupondispatchservice.modules.pilots.repository.PilotScheduleSpecifications;
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
public class PilotScheduleServiceImpl implements PilotScheduleService {

    private final PilotScheduleRepository pilotScheduleRepository;
    private final PilotRepository pilotRepository;

    @Override
    @HasPermission(permission = PermissionConstant.BOOKING_ASSIGN_PILOT)
    @Transactional
    public PilotSchedule assignPilotToBooking(Long pilotId, Booking booking) {
        Pilot pilot = pilotRepository.findById(pilotId)
                .orElseThrow(() -> new ResourceNotFoundException("Pilot not found with id: '" + pilotId + "'"));

        if (pilot.getStatus() != PilotStatus.AVAILABLE) {
            throw new InvalidRequestException("Pilot '" + pilot.getPilotCode() + "' is " + pilot.getStatus() + " and cannot be assigned");
        }

        List<PilotSchedule> conflicts = pilotScheduleRepository.findOverlapping(pilot.getId(), booking.getServiceStart(), booking.getServiceEnd());

        if (!conflicts.isEmpty()) {
            Long conflictingBookingId = conflicts.get(0).getBooking().getId();
            throw new InvalidRequestException("Pilot " + pilot.getPilotCode() + " is already scheduled for booking id " + conflictingBookingId + " in this window");
        }

        PilotSchedule schedule = new PilotSchedule();
        schedule.setPilot(pilot);
        schedule.setBooking(booking);
        schedule.setRouteCode(booking.getRoute().getRouteCode());
        schedule.setServiceStart(booking.getServiceStart());
        schedule.setServiceEnd(booking.getServiceEnd());
        schedule.setAssignedAt(LocalDateTime.now());

        return pilotScheduleRepository.save(schedule);
    }

    @Override
    @HasPermission(permission = PermissionConstant.PILOT_SCHEDULE_VIEW)
    public Page<PilotScheduleResponse> listSchedules(Long pilotId, Long bookingId, String routeCode, BookingStatus status, LocalDateTime from, LocalDateTime to, Pageable pageable) {
        Specification<PilotSchedule> specification = where(PilotScheduleSpecifications.pilotIdEquals(pilotId))
                .and(PilotScheduleSpecifications.bookingIdEquals(bookingId))
                .and(PilotScheduleSpecifications.routeCodeEquals(routeCode))
                .and(PilotScheduleSpecifications.statusEquals(status))
                .and(PilotScheduleSpecifications.serviceStartFrom(from))
                .and(PilotScheduleSpecifications.serviceStartTo(to))
                .and(PilotScheduleSpecifications.fetchAssociations());

        return pilotScheduleRepository.findAll(specification, pageable).map(PilotScheduleMapper::toResponse);
    }

    @Override
    @HasPermission(permission = PermissionConstant.PILOT_SCHEDULE_VIEW)
    public Page<PilotScheduleResponse> listPilotSchedules(Long pilotId, ScheduleStatus status, LocalDateTime from, LocalDateTime to, Pageable pageable) {
        Specification<PilotSchedule> specification = where(PilotScheduleSpecifications.pilotIdEquals(pilotId))
                .and(PilotScheduleSpecifications.statusEquals(status))
                .and(PilotScheduleSpecifications.serviceStartFrom(from))
                .and(PilotScheduleSpecifications.serviceStartTo(to))
                .and(PilotScheduleSpecifications.fetchAssociations());

        return pilotScheduleRepository.findAll(specification, pageable).map(PilotScheduleMapper::toResponse);
    }
}
