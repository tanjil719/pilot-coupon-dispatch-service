package com.pilotcoupondispatchservice.modules.coupons.controller;

import com.pilotcoupondispatchservice.enums.CouponRequestStatus;
import com.pilotcoupondispatchservice.modules.coupons.dto.CouponRequestCreateRequest;
import com.pilotcoupondispatchservice.modules.coupons.dto.CouponRequestResponse;
import com.pilotcoupondispatchservice.modules.coupons.service.CouponRequestService;
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
@RequestMapping("${url.base}/secured/owner/coupon-requests")
@AllArgsConstructor
public class OwnerCouponRequestController {

    private final CouponRequestService couponRequestService;

    @PostMapping("/create")
    public ResponseEntity<?> createCouponRequest(@Valid @RequestBody CouponRequestCreateRequest request) {
        CouponRequestResponse response = couponRequestService.createCouponRequest(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<?> listOwnRequests(
            @RequestParam(required = false) CouponRequestStatus status,
            @RequestParam(required = false) @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss") LocalDateTime from,
            @RequestParam(required = false) @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss") LocalDateTime to,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") Sort.Direction sortDir) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDir, sortBy));
        Page<CouponRequestResponse> requests = couponRequestService.listOwnRequests(status, from, to, pageable);
        return ResponseEntity.ok(requests);
    }

    @GetMapping("/by-id/{id}")
    public ResponseEntity<?> getOwnRequest(@PathVariable Long id) {
        CouponRequestResponse response = couponRequestService.getOwnRequest(id);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/cancel/{id}")
    public ResponseEntity<?> cancelOwnRequest(@PathVariable Long id) {
        CouponRequestResponse response = couponRequestService.cancelOwnRequest(id);
        return ResponseEntity.ok(response);
    }
}
