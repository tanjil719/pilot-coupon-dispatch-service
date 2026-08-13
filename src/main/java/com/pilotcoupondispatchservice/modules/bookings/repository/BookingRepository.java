package com.pilotcoupondispatchservice.modules.bookings.repository;

import com.pilotcoupondispatchservice.enums.BookingStatus;
import com.pilotcoupondispatchservice.modules.bookings.entity.Booking;
import org.springframework.data.domain.Pageable;
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

    // Dashboard: one grouped query per status instead of one count query per status.
    @Query("SELECT b.status, COUNT(b) FROM Booking b WHERE b.owner.id = :ownerId GROUP BY b.status")
    List<Object[]> countGroupedByStatusForOwner(@Param("ownerId") Long ownerId);

    @Query("SELECT b.status, COUNT(b) FROM Booking b GROUP BY b.status")
    List<Object[]> countGroupedByStatus();

    @Query("SELECT COUNT(b) FROM Booking b WHERE b.serviceStart >= :startOfDay AND b.serviceStart < :startOfNextDay")
    long countByServiceStartBetween(@Param("startOfDay") LocalDateTime startOfDay, @Param("startOfNextDay") LocalDateTime startOfNextDay);

    @Query("SELECT b FROM Booking b JOIN FETCH b.route JOIN FETCH b.vehicle LEFT JOIN FETCH b.pilot " +
            "WHERE b.owner.id = :ownerId " +
            "AND b.status IN (com.pilotcoupondispatchservice.enums.BookingStatus.APPROVED, com.pilotcoupondispatchservice.enums.BookingStatus.IN_PROGRESS) " +
            "AND b.serviceStart > :now ORDER BY b.serviceStart ASC")
    List<Booking> findUpcomingForOwner(@Param("ownerId") Long ownerId, @Param("now") LocalDateTime now, Pageable pageable);

    @Query("SELECT b FROM Booking b JOIN FETCH b.route WHERE b.owner.id = :ownerId ORDER BY b.createdAt DESC")
    List<Booking> findRecentForOwner(@Param("ownerId") Long ownerId, Pageable pageable);

    @Query("SELECT b FROM Booking b JOIN FETCH b.owner JOIN FETCH b.vehicle JOIN FETCH b.route LEFT JOIN FETCH b.pilot ORDER BY b.createdAt DESC")
    List<Booking> findRecentForAdmin(Pageable pageable);

    @Query("SELECT " +
            "COALESCE(SUM(b.route.serviceFee), 0.0), " +
            "COALESCE(SUM(CASE WHEN b.coupon IS NOT NULL AND b.coupon.amount > b.route.serviceFee " +
            "THEN b.coupon.amount - b.route.serviceFee ELSE 0.0 END), 0.0) " +
            "FROM Booking b WHERE b.paymentStatus = com.pilotcoupondispatchservice.enums.PaymentStatus.PAID")
    List<Object[]> paymentTotals();
}
