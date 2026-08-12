package com.pilotcoupondispatchservice.modules.coupons.service;

import com.pilotcoupondispatchservice.annotations.HasPermission;
import com.pilotcoupondispatchservice.constants.PermissionConstant;
import com.pilotcoupondispatchservice.enums.CouponRequestStatus;
import com.pilotcoupondispatchservice.exceptions.InvalidRequestException;
import com.pilotcoupondispatchservice.exceptions.ResourceAlreadyExistException;
import com.pilotcoupondispatchservice.exceptions.ResourceNotFoundException;
import com.pilotcoupondispatchservice.modules.coupons.dto.CouponRequestAdminResponse;
import com.pilotcoupondispatchservice.modules.coupons.dto.CouponRequestCreateRequest;
import com.pilotcoupondispatchservice.modules.coupons.dto.CouponRequestRejectRequest;
import com.pilotcoupondispatchservice.modules.coupons.dto.CouponRequestResponse;
import com.pilotcoupondispatchservice.modules.coupons.entity.CouponRequest;
import com.pilotcoupondispatchservice.modules.coupons.mapper.CouponRequestMapper;
import com.pilotcoupondispatchservice.modules.coupons.repository.CouponRequestRepository;
import com.pilotcoupondispatchservice.modules.coupons.repository.CouponRequestSpecifications;
import com.pilotcoupondispatchservice.modules.routes.entity.Route;
import com.pilotcoupondispatchservice.modules.routes.repository.RouteRepository;
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
public class CouponRequestServiceImpl implements CouponRequestService {

    private final CouponRequestRepository couponRequestRepository;
    private final RouteRepository routeRepository;
    private final UserRepository userRepository;

    @Override
    @HasPermission(permission = PermissionConstant.COUPON_REQUEST_CREATE)
    public CouponRequestResponse createCouponRequest(CouponRequestCreateRequest request) {
        Long ownerId = SecurityUtil.getLoggedInUserId();

        Route route = routeRepository.findByRouteCodeIgnoreCase(request.getRouteCode().trim())
                .orElseThrow(() -> new ResourceNotFoundException("Route not found with route code: '" + request.getRouteCode()));

        if (!route.getActive()) {
            throw new InvalidRequestException("Route " + route.getRouteCode() + " is not active at this moment.");
        }

        LocalDateTime serviceStart = request.getServiceStart();
        if (!serviceStart.isAfter(LocalDateTime.now())) {
            throw new InvalidRequestException("Service start must be in the future");
        }

        if (couponRequestRepository.existsByOwnerIdAndRouteCodeIgnoreCaseAndStatusAndServiceStart(ownerId, route.getRouteCode(), CouponRequestStatus.PENDING, serviceStart)) {
            throw new ResourceAlreadyExistException("A pending coupon request already exists same time for this owner on route: " + route.getRouteCode());
        }

        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: '" + ownerId));

        if (!owner.getIsActive()) {
            throw new InvalidRequestException("User is not active at this moment.");
        }

        CouponRequest couponRequest = new CouponRequest();
        couponRequest.setOwner(owner);
        couponRequest.setRouteCode(route.getRouteCode());
        couponRequest.setServiceStart(serviceStart);
        couponRequest.setRequestedAmount(route.getServiceFee());
        couponRequest.setNote(request.getNote());
        couponRequest.setStatus(CouponRequestStatus.PENDING);

        return CouponRequestMapper.toResponse(couponRequestRepository.save(couponRequest));
    }

    @Override
    @HasPermission(permission = PermissionConstant.COUPON_REQUEST_VIEW_OWN)
    public Page<CouponRequestResponse> listOwnRequests(CouponRequestStatus status, LocalDateTime from, LocalDateTime to, Pageable pageable) {
        Long ownerId = SecurityUtil.getLoggedInUserId();

        Specification<CouponRequest> specification = where(CouponRequestSpecifications.ownerIdEquals(ownerId))
                .and(CouponRequestSpecifications.statusEquals(status))
                .and(CouponRequestSpecifications.createdAtFrom(from))
                .and(CouponRequestSpecifications.createdAtTo(to));

        Page<CouponRequest> page = couponRequestRepository.findAll(specification, pageable);

        return page.map(CouponRequestMapper::toResponse);
    }

    @Override
    @HasPermission(permission = PermissionConstant.COUPON_REQUEST_VIEW_OWN)
    public CouponRequestResponse getOwnRequest(Long id) {
        CouponRequest couponRequest = findByIdAndOwnerId(id);
        return CouponRequestMapper.toResponse(couponRequest);
    }

    @Override
    @HasPermission(permission = PermissionConstant.COUPON_REQUEST_CANCEL)
    public CouponRequestResponse cancelOwnRequest(Long id) {
        CouponRequest couponRequest = findByIdAndOwnerId(id);

        if (couponRequest.getStatus() != CouponRequestStatus.PENDING) {
            throw new InvalidRequestException("Only a PENDING request can be cancelled, current status is " + couponRequest.getStatus());
        }

        couponRequest.setStatus(CouponRequestStatus.CANCELLED);
        return CouponRequestMapper.toResponse(couponRequestRepository.save(couponRequest));
    }

    @Override
    @HasPermission(permission = PermissionConstant.COUPON_REQUEST_VIEW_ALL)
    public Page<CouponRequestAdminResponse> listAllRequests(CouponRequestStatus status, Long ownerId, String routeCode, LocalDateTime from, LocalDateTime to, String search, Pageable pageable) {
//        CouponRequestStatus effectiveStatus = status == null ? CouponRequestStatus.PENDING : status;

        Specification<CouponRequest> specification = where(CouponRequestSpecifications.statusEquals(status))
                .and(CouponRequestSpecifications.ownerIdEquals(ownerId))
                .and(CouponRequestSpecifications.routeCodeEquals(routeCode))
                .and(CouponRequestSpecifications.createdAtFrom(from))
                .and(CouponRequestSpecifications.createdAtTo(to));

        Page<CouponRequest> page = couponRequestRepository.findAll(specification, pageable);

        return page.map(CouponRequestMapper::toAdminResponse);
    }

    @Override
    @HasPermission(permission = PermissionConstant.COUPON_REQUEST_VIEW_ALL)
    public CouponRequestAdminResponse getRequest(Long id) {
        CouponRequest couponRequest = findByIdOrThrow(id);
        return CouponRequestMapper.toAdminResponse(couponRequest);
    }

    @Override
    @HasPermission(permission = PermissionConstant.COUPON_REQUEST_REJECT)
    public CouponRequestAdminResponse rejectRequest(Long id, CouponRequestRejectRequest request) {
        CouponRequest couponRequest = findByIdOrThrow(id);

        if (couponRequest.getStatus() != CouponRequestStatus.PENDING) {
            throw new InvalidRequestException("Only a PENDING request can be rejected, current status is " + couponRequest.getStatus());
        }

        couponRequest.setStatus(CouponRequestStatus.REJECTED);
        couponRequest.setRejectionReason(request.getReason());
        couponRequest.setReviewedAt(LocalDateTime.now());

        CouponRequest saved = couponRequestRepository.save(couponRequest);
        return CouponRequestMapper.toAdminResponse(saved);
    }

    //*********** Internal Helper Methods ***********//

    private CouponRequest findByIdAndOwnerId(Long id) {
        Long ownerId = SecurityUtil.getLoggedInUserId();
        return couponRequestRepository.findByIdAndOwnerId(id, ownerId)
                .orElseThrow(() -> new ResourceNotFoundException("COUPON_REQUEST_NOT_FOUND: Coupon request not found with id: '" + id + "'"));
    }

    private CouponRequest findByIdOrThrow(Long id) {
        return couponRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("COUPON_REQUEST_NOT_FOUND: Coupon request not found with id: '" + id + "'"));
    }
}
