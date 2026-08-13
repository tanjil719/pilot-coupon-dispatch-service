package com.pilotcoupondispatchservice.modules.coupons.repository;

import com.pilotcoupondispatchservice.modules.coupons.entity.Coupon;
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
public interface CouponRepository extends JpaRepository<Coupon, Long>, JpaSpecificationExecutor<Coupon> {

    Optional<Coupon> findByIdAndOwnerId(Long id, Long ownerId);

    @Query("SELECT c FROM Coupon c JOIN FETCH c.owner WHERE c.id = :id")
    Optional<Coupon> findByIdWithOwner(@Param("id") Long id);

    boolean existsByCode(String code);

    Optional<Coupon> findByCodeAndOwnerId(String code, Long ownerId);

    // Dashboard: one aggregate query for the whole owner coupon stats group. NOT_USED is the
    // persisted "active" state; ACTIVE vs EXPIRED is split at read-time by expiresAt, per the same
    // rule the rest of the app uses (a coupon is never flipped to a separate EXPIRED status).
    // Tuple: [activeCount, activeTotalAmount, reservedCount, usedCount, expiredCount, expiringSoonCount]
    @Query("SELECT " +
            "COALESCE(SUM(CASE WHEN c.status = com.pilotcoupondispatchservice.enums.CouponStatus.NOT_USED AND c.expiresAt > :now THEN 1 ELSE 0 END), 0), " +
            "COALESCE(SUM(CASE WHEN c.status = com.pilotcoupondispatchservice.enums.CouponStatus.NOT_USED AND c.expiresAt > :now THEN c.amount ELSE 0.0 END), 0.0), " +
            "COALESCE(SUM(CASE WHEN c.status = com.pilotcoupondispatchservice.enums.CouponStatus.RESERVED THEN 1 ELSE 0 END), 0), " +
            "COALESCE(SUM(CASE WHEN c.status = com.pilotcoupondispatchservice.enums.CouponStatus.USED THEN 1 ELSE 0 END), 0), " +
            "COALESCE(SUM(CASE WHEN c.status = com.pilotcoupondispatchservice.enums.CouponStatus.NOT_USED AND c.expiresAt <= :now THEN 1 ELSE 0 END), 0), " +
            "COALESCE(SUM(CASE WHEN c.status = com.pilotcoupondispatchservice.enums.CouponStatus.NOT_USED AND c.expiresAt > :now AND c.expiresAt <= :soon THEN 1 ELSE 0 END), 0) " +
            "FROM Coupon c WHERE c.owner.id = :ownerId")
    List<Object[]> ownerCouponStats(@Param("ownerId") Long ownerId, @Param("now") LocalDateTime now, @Param("soon") LocalDateTime soon);

    // Dashboard: one aggregate query for the whole admin coupon stats group.
    // Tuple: [issuedCount, issuedTotalAmount, activeCount, activeTotalAmount, reservedCount, usedCount, usedTotalAmount, expiredCount]
    @Query("SELECT " +
            "COUNT(c), " +
            "COALESCE(SUM(c.amount), 0.0), " +
            "COALESCE(SUM(CASE WHEN c.status = com.pilotcoupondispatchservice.enums.CouponStatus.NOT_USED AND c.expiresAt > :now THEN 1 ELSE 0 END), 0), " +
            "COALESCE(SUM(CASE WHEN c.status = com.pilotcoupondispatchservice.enums.CouponStatus.NOT_USED AND c.expiresAt > :now THEN c.amount ELSE 0.0 END), 0.0), " +
            "COALESCE(SUM(CASE WHEN c.status = com.pilotcoupondispatchservice.enums.CouponStatus.RESERVED THEN 1 ELSE 0 END), 0), " +
            "COALESCE(SUM(CASE WHEN c.status = com.pilotcoupondispatchservice.enums.CouponStatus.USED THEN 1 ELSE 0 END), 0), " +
            "COALESCE(SUM(CASE WHEN c.status = com.pilotcoupondispatchservice.enums.CouponStatus.USED THEN c.amount ELSE 0.0 END), 0.0), " +
            "COALESCE(SUM(CASE WHEN c.status = com.pilotcoupondispatchservice.enums.CouponStatus.NOT_USED AND c.expiresAt <= :now THEN 1 ELSE 0 END), 0) " +
            "FROM Coupon c")
    List<Object[]> adminCouponStats(@Param("now") LocalDateTime now);

    // Dashboard action-required: ACTIVE coupons (NOT_USED, not yet expired) expiring within the window.
    @Query("SELECT c FROM Coupon c WHERE c.owner.id = :ownerId " +
            "AND c.status = com.pilotcoupondispatchservice.enums.CouponStatus.NOT_USED " +
            "AND c.expiresAt > :now AND c.expiresAt <= :soon ORDER BY c.expiresAt ASC")
    List<Coupon> findExpiringSoonForOwner(@Param("ownerId") Long ownerId,
                                           @Param("now") LocalDateTime now,
                                           @Param("soon") LocalDateTime soon,
                                           Pageable pageable);
}
