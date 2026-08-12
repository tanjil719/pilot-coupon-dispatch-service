package com.pilotcoupondispatchservice.modules.pilots.controller;

import com.pilotcoupondispatchservice.enums.PilotStatus;
import com.pilotcoupondispatchservice.enums.ScheduleStatus;
import com.pilotcoupondispatchservice.modules.pilots.dto.AvailablePilotResponse;
import com.pilotcoupondispatchservice.modules.pilots.dto.PilotCreateRequest;
import com.pilotcoupondispatchservice.modules.pilots.dto.PilotDetailResponse;
import com.pilotcoupondispatchservice.modules.pilots.dto.PilotResponse;
import com.pilotcoupondispatchservice.modules.pilots.dto.PilotScheduleResponse;
import com.pilotcoupondispatchservice.modules.pilots.dto.PilotStatusRequest;
import com.pilotcoupondispatchservice.modules.pilots.dto.PilotUpdateRequest;
import com.pilotcoupondispatchservice.modules.pilots.service.PilotScheduleService;
import com.pilotcoupondispatchservice.modules.pilots.service.PilotService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("${url.base}/secured/pilots")
@AllArgsConstructor
public class AdminPilotController {

    private final PilotService pilotService;
    private final PilotScheduleService pilotScheduleService;

    @PostMapping("/create")
    public ResponseEntity<?> createPilot(@Valid @RequestBody PilotCreateRequest request) {
        PilotResponse response = pilotService.createPilot(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<?> listPilots(
            @RequestParam(required = false) PilotStatus status,
            @RequestParam(defaultValue = "") String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") Sort.Direction sortDir) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDir, sortBy));
        Page<PilotResponse> pilots = pilotService.listPilots(status, search, pageable);
        return ResponseEntity.ok(pilots);
    }

    @GetMapping("/available")
    public ResponseEntity<?> findAvailablePilots(
            @RequestParam @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss") LocalDateTime start,
            @RequestParam @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss") LocalDateTime end) {

        List<AvailablePilotResponse> pilots = pilotService.findAvailablePilots(start, end);
        return ResponseEntity.ok(pilots);
    }

    @GetMapping("/by-id/{id}")
    public ResponseEntity<?> getPilot(@PathVariable Long id) {
        PilotDetailResponse response = pilotService.getPilot(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePilot(@PathVariable Long id, @Valid @RequestBody PilotUpdateRequest request) {
        PilotResponse response = pilotService.updatePilot(id, request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<?> changeStatus(@PathVariable Long id, @Valid @RequestBody PilotStatusRequest request) {
        PilotResponse response = pilotService.changeStatus(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePilot(@PathVariable Long id) {
        boolean deleted = pilotService.deletePilot(id);
        return ResponseEntity.ok(deleted);
    }

    @GetMapping("/schedules/{id}")
    public ResponseEntity<?> getPilotSchedules(
            @PathVariable Long id,
            @RequestParam(required = false) ScheduleStatus status,
            @RequestParam(required = false) @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss") LocalDateTime from,
            @RequestParam(required = false) @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss") LocalDateTime to,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "serviceStart") String sortBy,
            @RequestParam(defaultValue = "DESC") Sort.Direction sortDir) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDir, sortBy));
        Page<PilotScheduleResponse> schedules = pilotScheduleService.listPilotSchedules(id, status, from, to, pageable);
        return ResponseEntity.ok(schedules);
    }

    @GetMapping("/available-pilots/{id}")
    public ResponseEntity<?> getAvailablePilots(@PathVariable Long id) {
        List<AvailablePilotResponse> pilots = pilotService.findAvailablePilotsForBooking(id);
        return ResponseEntity.ok(pilots);
    }
}
