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

    // Referenced by VehicleServiceImpl#deleteVehicle to block soft delete while an active booking
    // references the vehicle.
    boolean existsByVehicleIdAndStatusIn(Long vehicleId, List<BookingStatus> statuses);

    // Overlap test: existingStart is before newEnd AND existingEnd is after newStart. Used at
    // booking creation to reject a vehicle that already has an APPROVED booking whose window
    // overlaps the requested one. PENDING_APPROVAL does not hold the vehicle's schedule yet, and
    // REJECTED/CANCELLED bookings never do, so callers should only pass APPROVED here.
    @Query("SELECT COUNT(b) > 0 FROM Booking b WHERE b.vehicle.id = :vehicleId AND b.status IN :statuses " +
            "AND b.serviceStart < :newEnd AND b.serviceEnd > :newStart")
    boolean existsOverlappingBooking(@Param("vehicleId") Long vehicleId,
                                      @Param("statuses") List<BookingStatus> statuses,
                                      @Param("newStart") LocalDateTime newStart,
                                      @Param("newEnd") LocalDateTime newEnd);

    // TODO(next-module): once the Pilot module exists, uncomment so the admin approval dropdown can
    // only ever show pilots with no overlapping APPROVED or IN_PROGRESS booking for the requested
    // window (same overlap test as existsOverlappingBooking above: existingStart is before newEnd
    // and existingEnd is after newStart). Until then, approval proceeds without a pilot and this
    // query is unused (see Booking#pilot and BookingServiceImpl#approveBooking).
    //
    // @Query("SELECT p FROM Pilot p WHERE p NOT IN (" +
    //         "SELECT b.pilot FROM Booking b WHERE b.pilot IS NOT NULL " +
    //         "AND b.status IN (com.pilotcoupondispatchservice.enums.BookingStatus.APPROVED, " +
    //         "com.pilotcoupondispatchservice.enums.BookingStatus.IN_PROGRESS) " +
    //         "AND b.serviceStart < :newEnd AND b.serviceEnd > :newStart)")
    // List<Pilot> findFreePilots(@Param("newStart") LocalDateTime newStart, @Param("newEnd") LocalDateTime newEnd);
}
