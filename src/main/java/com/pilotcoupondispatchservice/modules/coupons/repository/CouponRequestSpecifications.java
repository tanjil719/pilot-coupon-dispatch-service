package com.pilotcoupondispatchservice.modules.coupons.repository;

import com.pilotcoupondispatchservice.enums.CouponRequestStatus;
import com.pilotcoupondispatchservice.modules.coupons.entity.CouponRequest;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

public class CouponRequestSpecifications {

    private CouponRequestSpecifications() {
    }

    public static Specification<CouponRequest> ownerIdEquals(Long ownerId) {
        return (root, query, cb) -> (ownerId == null) ? cb.conjunction() : cb.equal(root.get("owner").get("id"), ownerId);
    }

    public static Specification<CouponRequest> routeCodeEquals(String routeCode) {
        return (root, query, cb) -> (!StringUtils.hasText(routeCode)) ? cb.conjunction() : cb.equal(cb.lower(root.get("routeCode")), routeCode.trim().toLowerCase());
    }

    public static Specification<CouponRequest> statusEquals(CouponRequestStatus status) {
        return (root, query, cb) -> (status == null) ? cb.conjunction() : cb.equal(root.get("status"), status);
    }

    public static Specification<CouponRequest> createdAtFrom(LocalDateTime from) {
        return (root, query, cb) -> (from == null) ? cb.conjunction() : cb.greaterThanOrEqualTo(root.get("createdAt"), from);
    }

    public static Specification<CouponRequest> createdAtTo(LocalDateTime to) {
        return (root, query, cb) -> (to == null) ? cb.conjunction() : cb.lessThanOrEqualTo(root.get("createdAt"), to);
    }

    public static Specification<CouponRequest> ownerSearch(String search) {
        return (root, query, cb) -> {
            if (!StringUtils.hasText(search)) {
                return cb.conjunction();
            }

            String pattern = "%" + search.trim().toLowerCase() + "%";

            return cb.or(
                    cb.like(cb.lower(root.get("owner").get("name")), pattern),
                    cb.like(cb.lower(root.get("owner").get("email")), pattern),
                    cb.like(root.get("owner").get("id").as(String.class), pattern)
            );
        };
    }

    public static Specification<CouponRequest> fetchOwner() {
        return (root, query, cb) -> {
            if (query.getResultType() != Long.class && query.getResultType() != long.class) {
                root.fetch("owner", JoinType.LEFT);
            }
            return cb.conjunction();
        };
    }
}
