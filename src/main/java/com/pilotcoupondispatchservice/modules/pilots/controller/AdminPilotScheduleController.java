package com.pilotcoupondispatchservice.modules.pilots.controller;

import com.pilotcoupondispatchservice.enums.BookingStatus;
import com.pilotcoupondispatchservice.enums.ScheduleStatus;
import com.pilotcoupondispatchservice.modules.pilots.dto.PilotScheduleResponse;
import com.pilotcoupondispatchservice.modules.pilots.service.PilotScheduleService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("${url.base}/secured/admin/pilot-schedules")
@AllArgsConstructor
public class AdminPilotScheduleController {

    private final PilotScheduleService pilotScheduleService;

    @GetMapping
    public ResponseEntity<?> listSchedules(
            @RequestParam(required = false) Long pilotId,
            @RequestParam(required = false) Long bookingId,
            @RequestParam(required = false) String routeCode,
            @RequestParam(required = false) BookingStatus status,
            @RequestParam(required = false) @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss") LocalDateTime from,
            @RequestParam(required = false) @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss") LocalDateTime to,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "serviceStart") String sortBy,
            @RequestParam(defaultValue = "DESC") Sort.Direction sortDir) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDir, sortBy));
        Page<PilotScheduleResponse> schedules = pilotScheduleService.listSchedules(pilotId, bookingId, routeCode, status, from, to, pageable);
        return ResponseEntity.ok(schedules);
    }
}
