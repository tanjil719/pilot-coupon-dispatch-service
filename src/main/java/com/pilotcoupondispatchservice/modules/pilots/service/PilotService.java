package com.pilotcoupondispatchservice.modules.pilots.service;

import com.pilotcoupondispatchservice.enums.PilotStatus;
import com.pilotcoupondispatchservice.modules.pilots.dto.AvailablePilotResponse;
import com.pilotcoupondispatchservice.modules.pilots.dto.PilotCreateRequest;
import com.pilotcoupondispatchservice.modules.pilots.dto.PilotDetailResponse;
import com.pilotcoupondispatchservice.modules.pilots.dto.PilotResponse;
import com.pilotcoupondispatchservice.modules.pilots.dto.PilotStatusRequest;
import com.pilotcoupondispatchservice.modules.pilots.dto.PilotUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface PilotService {

    PilotResponse createPilot(PilotCreateRequest request);

    Page<PilotResponse> listPilots(PilotStatus status, String search, Pageable pageable);

    PilotDetailResponse getPilot(Long id);

    PilotResponse updatePilot(Long id, PilotUpdateRequest request);

    PilotResponse changeStatus(Long id, PilotStatusRequest request);

    boolean deletePilot(Long id);

    List<AvailablePilotResponse> findAvailablePilots(LocalDateTime start, LocalDateTime end);

    List<AvailablePilotResponse> findAvailablePilotsForBooking(Long bookingId);
}
