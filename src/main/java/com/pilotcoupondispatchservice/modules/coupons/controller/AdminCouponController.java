package com.pilotcoupondispatchservice.modules.coupons.controller;

import com.pilotcoupondispatchservice.enums.CouponStatus;
import com.pilotcoupondispatchservice.modules.coupons.dto.CouponAdminResponse;
import com.pilotcoupondispatchservice.modules.coupons.dto.CouponCancelRequest;
import com.pilotcoupondispatchservice.modules.coupons.dto.CouponManualIssueRequest;
import com.pilotcoupondispatchservice.modules.coupons.dto.CouponUpdateRequest;
import com.pilotcoupondispatchservice.modules.coupons.service.CouponService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("${url.base}/secured/admin/coupons")
@AllArgsConstructor
public class AdminCouponController {

    private final CouponService couponService;

    @GetMapping
    public ResponseEntity<?> listAllCoupons(
            @RequestParam(required = false) CouponStatus status,
            @RequestParam(required = false) Long ownerId,
            @RequestParam(required = false) @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss") LocalDateTime from,
            @RequestParam(required = false) @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss") LocalDateTime to,
            @RequestParam(defaultValue = "") String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") Sort.Direction sortDir) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDir, sortBy));
        Page<CouponAdminResponse> coupons = couponService.listAllCoupons(status, ownerId, from, to, search, pageable);
        return ResponseEntity.ok(coupons);
    }

    @GetMapping("/by-id/{id}")
    public ResponseEntity<?> getCoupon(@PathVariable Long id) {
        CouponAdminResponse response = couponService.getCoupon(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/create")
    public ResponseEntity<?> manualIssueCoupon(@Valid @RequestBody CouponManualIssueRequest request) {
        CouponAdminResponse response = couponService.manualIssueCoupon(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/cancel/{id}")
    public ResponseEntity<?> cancelCoupon(@PathVariable Long id, @Valid @RequestBody CouponCancelRequest request) {
        CouponAdminResponse response = couponService.cancelCoupon(id, request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<?> updateCoupon(@PathVariable Long id, @Valid @RequestBody CouponUpdateRequest request) {
        CouponAdminResponse response = couponService.updateCoupon(id, request);
        return ResponseEntity.ok(response);
    }
}
