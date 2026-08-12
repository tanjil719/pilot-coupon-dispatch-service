package com.pilotcoupondispatchservice.modules.coupons.repository;

import com.pilotcoupondispatchservice.modules.coupons.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, Long>, JpaSpecificationExecutor<Coupon> {

    Optional<Coupon> findByIdAndOwnerId(Long id, Long ownerId);

    boolean existsByCode(String code);

    Optional<Coupon> findByCodeAndOwnerId(String code, Long ownerId);
}
