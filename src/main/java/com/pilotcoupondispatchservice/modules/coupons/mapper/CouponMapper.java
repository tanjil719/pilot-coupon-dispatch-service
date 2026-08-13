package com.pilotcoupondispatchservice.modules.coupons.mapper;

import com.pilotcoupondispatchservice.modules.coupons.dto.CouponAdminResponse;
import com.pilotcoupondispatchservice.modules.coupons.dto.CouponResponse;
import com.pilotcoupondispatchservice.modules.coupons.entity.Coupon;
import com.pilotcoupondispatchservice.modules.users.entity.User;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class CouponMapper {

    private CouponMapper() {
    }

    public static CouponResponse toResponse(Coupon coupon) {
        CouponResponse response = new CouponResponse();
        response.setId(coupon.getId());
        response.setCode(coupon.getCode());
        response.setAmount(coupon.getAmount());
        response.setStatus(coupon.getStatus());
        response.setCancelReason(coupon.getCancelReason());
        response.setExpiresAt(coupon.getExpiresAt());
        response.setUsedAt(coupon.getUsedAt());
        return response;
    }

    public static CouponAdminResponse toAdminResponse(Coupon coupon) {
        CouponAdminResponse response = new CouponAdminResponse();
        response.setId(coupon.getId());
        response.setCode(coupon.getCode());
        response.setAmount(coupon.getAmount());
        response.setStatus(coupon.getStatus());
        response.setExpiresAt(coupon.getExpiresAt());
        response.setUsedAt(coupon.getUsedAt());

        User owner = coupon.getOwner();
        response.setOwnerId(owner.getId());
        response.setOwnerName(owner.getName());
        response.setIssuedAt(coupon.getIssuedAt());
        return response;
    }
}
