package com.pilotcoupondispatchservice.modules.coupons.service;

import com.pilotcoupondispatchservice.annotations.HasPermission;
import com.pilotcoupondispatchservice.constants.PermissionConstant;
import com.pilotcoupondispatchservice.enums.CouponRequestStatus;
import com.pilotcoupondispatchservice.enums.CouponStatus;
import com.pilotcoupondispatchservice.enums.UserType;
import com.pilotcoupondispatchservice.exceptions.InvalidRequestException;
import com.pilotcoupondispatchservice.exceptions.ResourceNotFoundException;
import com.pilotcoupondispatchservice.modules.coupons.dto.CouponAdminResponse;
import com.pilotcoupondispatchservice.modules.coupons.dto.CouponCancelRequest;
import com.pilotcoupondispatchservice.modules.coupons.dto.CouponIssueRequest;
import com.pilotcoupondispatchservice.modules.coupons.dto.CouponManualIssueRequest;
import com.pilotcoupondispatchservice.modules.coupons.dto.CouponResponse;
import com.pilotcoupondispatchservice.modules.coupons.dto.CouponUpdateRequest;
import com.pilotcoupondispatchservice.modules.coupons.entity.Coupon;
import com.pilotcoupondispatchservice.modules.coupons.entity.CouponRequest;
import com.pilotcoupondispatchservice.modules.coupons.mapper.CouponMapper;
import com.pilotcoupondispatchservice.modules.coupons.repository.CouponRepository;
import com.pilotcoupondispatchservice.modules.coupons.repository.CouponRequestRepository;
import com.pilotcoupondispatchservice.modules.coupons.repository.CouponSpecifications;
import com.pilotcoupondispatchservice.modules.coupons.util.CouponCodeGenerator;
import com.pilotcoupondispatchservice.modules.users.entity.User;
import com.pilotcoupondispatchservice.modules.users.repository.UserRepository;
import com.pilotcoupondispatchservice.utils.SecurityUtil;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.springframework.data.jpa.domain.Specification.where;

@Service
@AllArgsConstructor
public class CouponServiceImpl implements CouponService {

    private static final int DEFAULT_VALIDITY_DAYS = 30;

    private final CouponRepository couponRepository;
    private final CouponRequestRepository couponRequestRepository;
    private final UserRepository userRepository;
    private final CouponCodeGenerator couponCodeGenerator;

    @Override
    @HasPermission(permission = PermissionConstant.COUPON_VIEW_OWN)
    public Page<CouponResponse> listOwnCoupons(CouponStatus status, Pageable pageable) {
        Long ownerId = SecurityUtil.getLoggedInUserId();

        Specification<Coupon> specification = where(CouponSpecifications.ownerIdEquals(ownerId))
                .and(CouponSpecifications.statusEquals(status));

        return couponRepository.findAll(specification, pageable).map(CouponMapper::toResponse);
    }

    @Override
    @HasPermission(permission = PermissionConstant.COUPON_VIEW_OWN)
    public CouponResponse getOwnCoupon(Long id) {
        Long ownerId = SecurityUtil.getLoggedInUserId();
        Coupon coupon = couponRepository.findByIdAndOwnerId(id, ownerId)
                .orElseThrow(() -> new ResourceNotFoundException("Coupon not found with id: '" + id + "'"));
        return CouponMapper.toResponse(coupon);
    }

    @Override
    @HasPermission(permission = PermissionConstant.COUPON_ISSUE)
    @Transactional
    public CouponAdminResponse issueCouponForRequest(Long requestId, CouponIssueRequest request) {
        CouponRequest couponRequest = couponRequestRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Coupon request not found with id: " + requestId));

        if (couponRequest.getStatus() != CouponRequestStatus.PENDING) {
            throw new InvalidRequestException("Only a PENDING request can be issued a coupon, current status is " + couponRequest.getStatus());
        }

        Double amount = request.getAmount() != null ? request.getAmount() : couponRequest.getRequestedAmount();
        if (amount.compareTo(couponRequest.getRequestedAmount()) < 0) {
            throw new InvalidRequestException("Coupon amount must be greater than or equal to the requested amount of " + couponRequest.getRequestedAmount());
        }

        LocalDateTime issuedAt = LocalDateTime.now();
        LocalDateTime expiresAt = issuedAt.plusDays(request.getValidityDays() != null ? request.getValidityDays() : DEFAULT_VALIDITY_DAYS);

        Coupon coupon = new Coupon();
        coupon.setCode(couponCodeGenerator.generateCouponCode());
        coupon.setOwner(couponRequest.getOwner());
        coupon.setAmount(amount);
        coupon.setStatus(CouponStatus.NOT_USED);
        coupon.setIssuedAt(issuedAt);
        coupon.setExpiresAt(expiresAt);

        Coupon savedCoupon = couponRepository.save(coupon);

        couponRequest.setStatus(CouponRequestStatus.APPROVED);
        couponRequest.setReviewedAt(issuedAt);
        couponRequestRepository.save(couponRequest);

        return CouponMapper.toAdminResponse(savedCoupon);
    }

    @Override
    @HasPermission(permission = PermissionConstant.COUPON_ISSUE)
    public CouponAdminResponse manualIssueCoupon(CouponManualIssueRequest request) {

        User targetOwner = userRepository.findByIdAndIsActiveTrue(request.getOwnerId())
                .filter(user -> user.getUserType() == UserType.OWNER)
                .orElseThrow(() -> new InvalidRequestException("Target owner not found by id: " + request.getOwnerId()));

        LocalDateTime issuedAt = LocalDateTime.now();
        LocalDateTime expiresAt = issuedAt.plusDays(request.getValidityDays() != null ? request.getValidityDays() : DEFAULT_VALIDITY_DAYS);

        Coupon coupon = new Coupon();
        coupon.setCode(couponCodeGenerator.generateCouponCode());
        coupon.setOwner(targetOwner);
        coupon.setAmount(request.getAmount());
        coupon.setStatus(CouponStatus.NOT_USED);
        coupon.setIssuedAt(issuedAt);
        coupon.setExpiresAt(expiresAt);

        return CouponMapper.toAdminResponse(couponRepository.save(coupon));
    }

    @Override
    @HasPermission(permission = PermissionConstant.COUPON_VIEW_ALL)
    public Page<CouponAdminResponse> listAllCoupons(CouponStatus status, Long ownerId, LocalDateTime from, LocalDateTime to, String search, Pageable pageable) {
        Specification<Coupon> specification = where(CouponSpecifications.statusEquals(status))
                .and(CouponSpecifications.ownerIdEquals(ownerId))
                .and(CouponSpecifications.issuedAtFrom(from))
                .and(CouponSpecifications.issuedAtTo(to))
                .and(CouponSpecifications.codeContains(search))
                .and(CouponSpecifications.fetchOwner());

        return couponRepository.findAll(specification, pageable).map(CouponMapper::toAdminResponse);
    }

    @Override
    @HasPermission(permission = PermissionConstant.COUPON_VIEW_ALL)
    public CouponAdminResponse getCoupon(Long id) {
        return CouponMapper.toAdminResponse(findByIdOrThrow(id));
    }

    @Override
    @HasPermission(permission = PermissionConstant.COUPON_CANCEL)
    public CouponAdminResponse cancelCoupon(Long id, CouponCancelRequest request) {
        Coupon coupon = findByIdOrThrow(id);

        if (coupon.getStatus() != CouponStatus.NOT_USED) {
            throw new InvalidRequestException("Only a NOT_USED coupon can be cancelled, current status is " + coupon.getStatus());
        }

        coupon.setStatus(CouponStatus.CANCELLED);
        return CouponMapper.toAdminResponse(couponRepository.save(coupon));
    }

    @Override
    @HasPermission(permission = PermissionConstant.COUPON_UPDATE)
    public CouponAdminResponse updateCoupon(Long id, CouponUpdateRequest request) {
        Coupon coupon = findByIdOrThrow(id);

        if (coupon.getStatus() != CouponStatus.NOT_USED) {
            throw new InvalidRequestException("Only a NOT_USED coupon can be updated, current status is " + coupon.getStatus());
        }

        coupon.setAmount(request.getAmount());
        coupon.setExpiresAt(request.getExpiresAt());
        return CouponMapper.toAdminResponse(couponRepository.save(coupon));
    }


    //*********** Internal Helper Methods ***********//

    private Coupon findByIdOrThrow(Long id) {
        return couponRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Coupon not found with id: " + id));
    }
}
