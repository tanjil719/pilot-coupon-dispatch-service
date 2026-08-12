package com.pilotcoupondispatchservice.modules.coupons.controller;

import com.pilotcoupondispatchservice.enums.CouponRequestStatus;
import com.pilotcoupondispatchservice.modules.coupons.dto.CouponIssueRequest;
import com.pilotcoupondispatchservice.modules.coupons.dto.CouponRequestAdminResponse;
import com.pilotcoupondispatchservice.modules.coupons.dto.CouponRequestRejectRequest;
import com.pilotcoupondispatchservice.modules.coupons.dto.CouponAdminResponse;
import com.pilotcoupondispatchservice.modules.coupons.service.CouponRequestService;
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
@RequestMapping("${url.base}/secured/admin/coupon-requests")
@AllArgsConstructor
public class AdminCouponRequestController {

    private final CouponRequestService couponRequestService;
    private final CouponService couponService;

    @GetMapping
    public ResponseEntity<?> listAllRequests(
            @RequestParam(required = false) CouponRequestStatus status,
            @RequestParam(required = false) Long ownerId,
            @RequestParam(required = false) String routeCode,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to,
            @RequestParam(defaultValue = "") String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") Sort.Direction sortDir) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDir, sortBy));
        Page<CouponRequestAdminResponse> requests = couponRequestService.listAllRequests(status, ownerId, routeCode, from, to, search, pageable);
        return ResponseEntity.ok(requests);
    }

    @GetMapping("/by-id/{id}")
    public ResponseEntity<?> getRequest(@PathVariable Long id) {
        CouponRequestAdminResponse response = couponRequestService.getRequest(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/issue/{id}")
    public ResponseEntity<?> issueCoupon(@PathVariable Long id, @Valid @RequestBody CouponIssueRequest request) {
        CouponAdminResponse response = couponService.issueCouponForRequest(id, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/reject/{id}")
    public ResponseEntity<?> rejectRequest(@PathVariable Long id, @Valid @RequestBody CouponRequestRejectRequest request) {
        CouponRequestAdminResponse response = couponRequestService.rejectRequest(id, request);
        return ResponseEntity.ok(response);
    }
}
