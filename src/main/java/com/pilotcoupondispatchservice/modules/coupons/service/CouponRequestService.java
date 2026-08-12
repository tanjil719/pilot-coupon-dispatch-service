package com.pilotcoupondispatchservice.modules.coupons.service;

import com.pilotcoupondispatchservice.enums.CouponRequestStatus;
import com.pilotcoupondispatchservice.modules.coupons.dto.CouponRequestAdminResponse;
import com.pilotcoupondispatchservice.modules.coupons.dto.CouponRequestCreateRequest;
import com.pilotcoupondispatchservice.modules.coupons.dto.CouponRequestRejectRequest;
import com.pilotcoupondispatchservice.modules.coupons.dto.CouponRequestResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface CouponRequestService {

    // ---- Owner ----

    CouponRequestResponse createCouponRequest(CouponRequestCreateRequest request);

    Page<CouponRequestResponse> listOwnRequests(CouponRequestStatus status, LocalDateTime from, LocalDateTime to, Pageable pageable);

    CouponRequestResponse getOwnRequest(Long id);

    CouponRequestResponse cancelOwnRequest(Long id);

    // ---- Admin ----

    Page<CouponRequestAdminResponse> listAllRequests(CouponRequestStatus status, Long ownerId, String routeCode, LocalDateTime from, LocalDateTime to, String search, Pageable pageable);

    CouponRequestAdminResponse getRequest(Long id);

    CouponRequestAdminResponse rejectRequest(Long id, CouponRequestRejectRequest request);
}
