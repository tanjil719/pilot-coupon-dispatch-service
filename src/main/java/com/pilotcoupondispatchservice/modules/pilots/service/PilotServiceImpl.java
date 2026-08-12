package com.pilotcoupondispatchservice.modules.pilots.service;

import com.pilotcoupondispatchservice.annotations.HasPermission;
import com.pilotcoupondispatchservice.constants.PermissionConstant;
import com.pilotcoupondispatchservice.enums.PilotStatus;
import com.pilotcoupondispatchservice.exceptions.InvalidRequestException;
import com.pilotcoupondispatchservice.exceptions.ResourceNotFoundException;
import com.pilotcoupondispatchservice.modules.bookings.entity.Booking;
import com.pilotcoupondispatchservice.modules.bookings.repository.BookingRepository;
import com.pilotcoupondispatchservice.modules.pilots.dto.AvailablePilotResponse;
import com.pilotcoupondispatchservice.modules.pilots.dto.PilotCreateRequest;
import com.pilotcoupondispatchservice.modules.pilots.dto.PilotDetailResponse;
import com.pilotcoupondispatchservice.modules.pilots.dto.PilotResponse;
import com.pilotcoupondispatchservice.modules.pilots.dto.PilotStatusRequest;
import com.pilotcoupondispatchservice.modules.pilots.dto.PilotUpdateRequest;
import com.pilotcoupondispatchservice.modules.pilots.entity.Pilot;
import com.pilotcoupondispatchservice.modules.pilots.entity.PilotSchedule;
import com.pilotcoupondispatchservice.modules.pilots.mapper.PilotMapper;
import com.pilotcoupondispatchservice.modules.pilots.repository.PilotRepository;
import com.pilotcoupondispatchservice.modules.pilots.repository.PilotScheduleRepository;
import com.pilotcoupondispatchservice.modules.pilots.repository.PilotSpecifications;
import com.pilotcoupondispatchservice.modules.pilots.util.PilotCodeGenerator;
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
public class PilotServiceImpl implements PilotService {

    private final PilotRepository pilotRepository;
    private final PilotScheduleRepository pilotScheduleRepository;
    private final BookingRepository bookingRepository;
    private final PilotCodeGenerator pilotCodeGenerator;

    @Override
    @HasPermission(permission = PermissionConstant.PILOT_CREATE)
    public PilotResponse createPilot(PilotCreateRequest request) {

        String licenseNo = normalizeUpper(request.getLicenseNo());

        if (pilotRepository.existsByLicenseNo(licenseNo)) {
            throw new InvalidRequestException("A pilot already exists with license number " + licenseNo);
        }

        Pilot pilot = new Pilot();
        pilot.setPilotCode(pilotCodeGenerator.generatePilotCode());
        pilot.setName(request.getName());
        pilot.setLicenseNo(licenseNo);
        pilot.setPhone(request.getPhone());
        pilot.setExperienceYears(request.getExperienceYears());
        pilot.setStatus(PilotStatus.AVAILABLE);

        return PilotMapper.toResponse(pilotRepository.save(pilot));
    }

    @Override
    @HasPermission(permission = PermissionConstant.PILOT_VIEW_ALL)
    public Page<PilotResponse> listPilots(PilotStatus status, String search, Pageable pageable) {

        Specification<Pilot> specification = where(PilotSpecifications.statusEquals(status))
                .and(PilotSpecifications.searchContains(search));

        return pilotRepository.findAll(specification, pageable).map(PilotMapper::toResponse);
    }

    @Override
    @HasPermission(permission = PermissionConstant.PILOT_VIEW_ALL)
    public PilotDetailResponse getPilot(Long id) {

        Pilot pilot = findByIdOrThrow(id);

        LocalDateTime now = LocalDateTime.now();
        long scheduledCount = pilotScheduleRepository.countByPilotIdAndServiceStartAfter(id, now);
        long inProgressCount = pilotScheduleRepository.countByPilotIdAndServiceStartLessThanEqualAndServiceEndAfter(id, now, now);
        long completedCount = pilotScheduleRepository.countByPilotIdAndServiceEndLessThanEqual(id, now);

        PilotSchedule nextSchedule = pilotScheduleRepository
                .findFirstByPilotIdAndServiceStartAfterOrderByServiceStartAsc(id, now)
                .orElse(null);

        return PilotMapper.toDetailResponse(pilot, scheduledCount, inProgressCount, completedCount, nextSchedule);
    }

    @Override
    @HasPermission(permission = PermissionConstant.PILOT_UPDATE)
    public PilotResponse updatePilot(Long id, PilotUpdateRequest request) {
        Pilot pilot = findByIdOrThrow(id);

        String licenseNo = normalizeUpper(request.getLicenseNo());
        if (!licenseNo.equals(pilot.getLicenseNo()) && pilotRepository.existsByLicenseNoAndIdNot(licenseNo, id)) {
            throw new InvalidRequestException("A pilot already exists with license number " + licenseNo);
        }

        pilot.setName(request.getName());
        pilot.setLicenseNo(licenseNo);
        pilot.setPhone(request.getPhone());
        pilot.setExperienceYears(request.getExperienceYears());

        return PilotMapper.toResponse(pilotRepository.save(pilot));
    }

    @Override
    @HasPermission(permission = PermissionConstant.PILOT_CHANGE_STATUS)
    public PilotResponse changeStatus(Long id, PilotStatusRequest request) {
        Pilot pilot = findByIdOrThrow(id);

        if (request.getStatus() != PilotStatus.AVAILABLE) {
            assertNoActiveSchedule(id, "PILOT_HAS_ACTIVE_SCHEDULE");
        }

        pilot.setStatus(request.getStatus());

        return PilotMapper.toResponse(pilotRepository.save(pilot));
    }

    @Override
    @HasPermission(permission = PermissionConstant.PILOT_DELETE)
    public boolean deletePilot(Long id) {
        Pilot pilot = findByIdOrThrow(id);

        if (pilot.getStatus() == PilotStatus.INACTIVE) {
            return false;
        }

        assertNoActiveSchedule(id, "PILOT_IN_USE");

        pilot.setStatus(PilotStatus.INACTIVE);
        pilotRepository.save(pilot);
        return true;
    }

    @Override
    @HasPermission(permission = PermissionConstant.PILOT_VIEW_ALL)
    public List<AvailablePilotResponse> findAvailablePilots(LocalDateTime start, LocalDateTime end) {

        if (start == null || end == null || !start.isBefore(end)) {
            throw new InvalidRequestException("Start must be before end");
        }

        return pilotRepository.findAvailablePilots(start, end).stream()
                .map(pilot -> PilotMapper.toAvailableResponse(pilot, pilotScheduleRepository.countByPilotIdAndServiceEndLessThanEqual(pilot.getId(), LocalDateTime.now())))
                .toList();
    }

    @Override
    @HasPermission(permission = PermissionConstant.PILOT_VIEW_ALL)
    public List<AvailablePilotResponse> findAvailablePilotsForBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: '" + bookingId + "'"));

        return findAvailablePilots(booking.getServiceStart(), booking.getServiceEnd());
    }

    //*********** Internal Helper Methods ***********//

    private Pilot findByIdOrThrow(Long id) {
        return pilotRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pilot not found with id: '" + id + "'"));
    }

    private void assertNoActiveSchedule(Long pilotId, String errorCode) {
        List<Long> conflictingBookingIds = pilotRepository.findActiveScheduleBookingIds(pilotId, LocalDateTime.now());
        if (!conflictingBookingIds.isEmpty()) {
            throw new InvalidRequestException(errorCode + ": Pilot has active schedules for booking ids " + conflictingBookingIds + "; cancel or reassign those bookings first");
        }
    }

    private String normalizeUpper(String value) {
        return value == null || value.isBlank() ? null : value.trim().toUpperCase();
    }
}
