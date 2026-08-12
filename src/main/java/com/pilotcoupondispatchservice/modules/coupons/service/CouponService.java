package com.pilotcoupondispatchservice.modules.coupons.service;

import com.pilotcoupondispatchservice.enums.CouponStatus;
import com.pilotcoupondispatchservice.modules.coupons.dto.CouponAdminResponse;
import com.pilotcoupondispatchservice.modules.coupons.dto.CouponCancelRequest;
import com.pilotcoupondispatchservice.modules.coupons.dto.CouponIssueRequest;
import com.pilotcoupondispatchservice.modules.coupons.dto.CouponManualIssueRequest;
import com.pilotcoupondispatchservice.modules.coupons.dto.CouponResponse;
import com.pilotcoupondispatchservice.modules.coupons.dto.CouponUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface CouponService {

    // ---- Owner ----

    Page<CouponResponse> listOwnCoupons(CouponStatus status, Pageable pageable);

    CouponResponse getOwnCoupon(Long id);

    // ---- Admin ----
    CouponAdminResponse issueCouponForRequest(Long requestId, CouponIssueRequest request);

    CouponAdminResponse manualIssueCoupon(CouponManualIssueRequest request);

    Page<CouponAdminResponse> listAllCoupons(CouponStatus status, Long ownerId, LocalDateTime from, LocalDateTime to, String search, Pageable pageable);

    CouponAdminResponse getCoupon(Long id);

    CouponAdminResponse cancelCoupon(Long id, CouponCancelRequest request);

    CouponAdminResponse updateCoupon(Long id, CouponUpdateRequest request);
}
