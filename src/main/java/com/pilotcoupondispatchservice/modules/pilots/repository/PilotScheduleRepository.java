package com.pilotcoupondispatchservice.modules.pilots.repository;

import com.pilotcoupondispatchservice.modules.pilots.entity.PilotSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PilotScheduleRepository extends JpaRepository<PilotSchedule, Long>, JpaSpecificationExecutor<PilotSchedule> {

    long countByPilotIdAndServiceStartAfter(Long pilotId, LocalDateTime now);

    long countByPilotIdAndServiceStartLessThanEqualAndServiceEndAfter(Long pilotId, LocalDateTime start, LocalDateTime end);

    long countByPilotIdAndServiceEndLessThanEqual(Long pilotId, LocalDateTime now);

    Optional<PilotSchedule> findFirstByPilotIdAndServiceStartAfterOrderByServiceStartAsc(Long pilotId, LocalDateTime now);

    @Query("SELECT s FROM PilotSchedule s WHERE s.pilot.id = :pilotId " +
            "AND s.serviceEnd > CURRENT_TIMESTAMP " +
            "AND s.serviceStart < :end AND s.serviceEnd > :start")
    List<PilotSchedule> findOverlapping(@Param("pilotId") Long pilotId,
                                         @Param("start") LocalDateTime start,
                                         @Param("end") LocalDateTime end);
}
