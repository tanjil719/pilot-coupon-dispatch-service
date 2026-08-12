package com.pilotcoupondispatchservice.modules.coupons.controller;

import com.pilotcoupondispatchservice.enums.CouponStatus;
import com.pilotcoupondispatchservice.modules.coupons.dto.CouponResponse;
import com.pilotcoupondispatchservice.modules.coupons.service.CouponService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${url.base}/secured/owner/coupons")
@AllArgsConstructor
public class OwnerCouponController {

    private final CouponService couponService;

    @GetMapping
    public ResponseEntity<?> listOwnCoupons(
            @RequestParam(required = false) CouponStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") Sort.Direction sortDir) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDir, sortBy));
        Page<CouponResponse> coupons = couponService.listOwnCoupons(status, pageable);
        return ResponseEntity.ok(coupons);
    }

    @GetMapping("/by-id/{id}")
    public ResponseEntity<?> getOwnCoupon(@PathVariable Long id) {
        CouponResponse response = couponService.getOwnCoupon(id);
        return ResponseEntity.ok(response);
    }
}
