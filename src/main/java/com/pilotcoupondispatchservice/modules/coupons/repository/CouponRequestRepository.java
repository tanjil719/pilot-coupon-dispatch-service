package com.pilotcoupondispatchservice.modules.coupons.repository;

import com.pilotcoupondispatchservice.enums.CouponRequestStatus;
import com.pilotcoupondispatchservice.modules.coupons.entity.CouponRequest;
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
public interface CouponRequestRepository extends JpaRepository<CouponRequest, Long>, JpaSpecificationExecutor<CouponRequest> {

    Optional<CouponRequest> findByIdAndOwnerId(Long id, Long ownerId);

    @Query("SELECT cr FROM CouponRequest cr JOIN FETCH cr.owner WHERE cr.id = :id")
    Optional<CouponRequest> findByIdWithOwner(@Param("id") Long id);

    boolean existsByOwnerIdAndRouteCodeIgnoreCaseAndStatusAndServiceStartAfter(Long ownerId, String routeCode, CouponRequestStatus status, java.time.LocalDateTime serviceStart);

    boolean existsByOwnerIdAndRouteCodeIgnoreCaseAndStatusAndServiceStart(
            Long ownerId,
            String routeCode,
            CouponRequestStatus status,
            LocalDateTime serviceStart
    );

    long countByStatus(CouponRequestStatus status);

    @Query("SELECT cr.status, COUNT(cr) FROM CouponRequest cr WHERE cr.owner.id = :ownerId GROUP BY cr.status")
    List<Object[]> countGroupedByStatusForOwner(@Param("ownerId") Long ownerId);

    @Query("SELECT cr FROM CouponRequest cr WHERE cr.owner.id = :ownerId " +
            "AND cr.status = com.pilotcoupondispatchservice.enums.CouponRequestStatus.APPROVED " +
            "AND NOT EXISTS (SELECT 1 FROM Booking b WHERE b.owner.id = :ownerId " +
            "AND b.route.routeCode = cr.routeCode AND b.serviceStart = cr.serviceStart) " +
            "ORDER BY cr.reviewedAt DESC")
    List<CouponRequest> findApprovedWithoutBookingForOwner(@Param("ownerId") Long ownerId, Pageable pageable);

    @Query("SELECT cr FROM CouponRequest cr JOIN FETCH cr.owner " +
            "WHERE cr.status = com.pilotcoupondispatchservice.enums.CouponRequestStatus.PENDING ORDER BY cr.createdAt DESC")
    List<CouponRequest> findRecentPending(Pageable pageable);
}
