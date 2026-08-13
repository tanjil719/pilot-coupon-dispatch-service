package com.pilotcoupondispatchservice.modules.pilots.service;

import com.pilotcoupondispatchservice.enums.BookingStatus;
import com.pilotcoupondispatchservice.enums.ScheduleStatus;
import com.pilotcoupondispatchservice.modules.bookings.entity.Booking;
import com.pilotcoupondispatchservice.modules.pilots.dto.PilotScheduleResponse;
import com.pilotcoupondispatchservice.modules.pilots.entity.PilotSchedule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface PilotScheduleService {

    PilotSchedule assignPilotToBooking(Long pilotId, Booking booking);

    Page<PilotScheduleResponse> listSchedules(Long pilotId, Long bookingId, String routeCode, BookingStatus status, LocalDateTime from, LocalDateTime to, Pageable pageable);

    Page<PilotScheduleResponse> listPilotSchedules(Long pilotId, ScheduleStatus status, LocalDateTime from, LocalDateTime to, Pageable pageable);
}
