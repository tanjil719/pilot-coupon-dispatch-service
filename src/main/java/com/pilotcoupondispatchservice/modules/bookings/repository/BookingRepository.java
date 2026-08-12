package com.pilotcoupondispatchservice.modules.bookings.repository;

import com.pilotcoupondispatchservice.enums.BookingStatus;
import com.pilotcoupondispatchservice.modules.bookings.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long>, JpaSpecificationExecutor<Booking> {

    Optional<Booking> findByIdAndOwnerId(Long id, Long ownerId);

    boolean existsByVehicleIdAndStatusIn(Long vehicleId, List<BookingStatus> statuses);

    @Query("SELECT COUNT(b) > 0 FROM Booking b WHERE b.vehicle.id = :vehicleId AND b.status IN :statuses " +
            "AND b.serviceStart < :newEnd AND b.serviceEnd > :newStart")
    boolean existsOverlappingBooking(@Param("vehicleId") Long vehicleId,
                                     @Param("statuses") List<BookingStatus> statuses,
                                     @Param("newStart") LocalDateTime newStart,
                                     @Param("newEnd") LocalDateTime newEnd);
}
