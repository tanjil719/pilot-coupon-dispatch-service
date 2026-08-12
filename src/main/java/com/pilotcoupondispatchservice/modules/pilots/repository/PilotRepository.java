package com.pilotcoupondispatchservice.modules.pilots.repository;

import com.pilotcoupondispatchservice.modules.pilots.entity.Pilot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PilotRepository extends JpaRepository<Pilot, Long>, JpaSpecificationExecutor<Pilot> {

    boolean existsByPilotCode(String pilotCode);

    boolean existsByLicenseNo(String licenseNo);

    boolean existsByLicenseNoAndIdNot(String licenseNo, Long id);

    @Query("SELECT p FROM Pilot p WHERE p.status = com.pilotcoupondispatchservice.enums.PilotStatus.AVAILABLE " +
            "AND NOT EXISTS (SELECT 1 FROM PilotSchedule s WHERE s.pilot = p " +
            "AND s.serviceEnd > CURRENT_TIMESTAMP " +
            "AND s.serviceStart < :end AND s.serviceEnd > :start)")
    List<Pilot> findAvailablePilots(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT s.booking.id FROM PilotSchedule s WHERE s.pilot.id = :pilotId " +
            "AND s.serviceEnd > :now")
    List<Long> findActiveScheduleBookingIds(@Param("pilotId") Long pilotId, @Param("now") LocalDateTime now);
}
