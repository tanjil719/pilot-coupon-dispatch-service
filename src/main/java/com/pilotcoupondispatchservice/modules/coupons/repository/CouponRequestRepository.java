package com.pilotcoupondispatchservice.modules.coupons.repository;

import com.pilotcoupondispatchservice.enums.CouponRequestStatus;
import com.pilotcoupondispatchservice.modules.coupons.entity.CouponRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface CouponRequestRepository extends JpaRepository<CouponRequest, Long>, JpaSpecificationExecutor<CouponRequest> {

    Optional<CouponRequest> findByIdAndOwnerId(Long id, Long ownerId);

    boolean existsByOwnerIdAndRouteCodeIgnoreCaseAndStatusAndServiceStartAfter(Long ownerId, String routeCode, CouponRequestStatus status, java.time.LocalDateTime serviceStart);

    boolean existsByOwnerIdAndRouteCodeIgnoreCaseAndStatusAndServiceStart(
            Long ownerId,
            String routeCode,
            CouponRequestStatus status,
            LocalDateTime serviceStart
    );
}
