package com.pilotcoupondispatchservice.modules.coupons.mapper;

import com.pilotcoupondispatchservice.modules.coupons.dto.CouponRequestAdminResponse;
import com.pilotcoupondispatchservice.modules.coupons.dto.CouponRequestResponse;
import com.pilotcoupondispatchservice.modules.coupons.entity.CouponRequest;
import com.pilotcoupondispatchservice.modules.users.entity.User;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class CouponRequestMapper {

    private CouponRequestMapper() {
    }

    public static CouponRequestResponse toResponse(CouponRequest request) {
        CouponRequestResponse response = new CouponRequestResponse();
        response.setId(request.getId());
        response.setRouteCode(request.getRouteCode());
        response.setServiceStart(request.getServiceStart());
        response.setRequestedAmount(request.getRequestedAmount());
        response.setStatus(request.getStatus());
        response.setRejectionReason(request.getRejectionReason());
        response.setCreatedAt(request.getCreatedAt());
        return response;
    }

    public static CouponRequestAdminResponse toAdminResponse(CouponRequest request) {
        CouponRequestAdminResponse response = new CouponRequestAdminResponse();
        response.setId(request.getId());
        response.setRouteCode(request.getRouteCode());
        response.setServiceStart(request.getServiceStart());
        response.setRequestedAmount(request.getRequestedAmount());
        response.setStatus(request.getStatus());
        response.setRejectionReason(request.getRejectionReason());
        response.setCreatedAt(request.getCreatedAt());
        response.setCouponCode(request.getCoupon());

        User owner = request.getOwner();
        response.setOwnerId(owner.getId());
        response.setOwnerName(owner.getName());
        response.setOwnerEmail(owner.getEmail());

        response.setReviewedAt(request.getReviewedAt());
        return response;
    }

//    private static String formatAmount(Double amount) {
//        return amount == null ? null : BigDecimal.valueOf(amount).setScale(2, RoundingMode.HALF_UP).toPlainString();
//    }
}
