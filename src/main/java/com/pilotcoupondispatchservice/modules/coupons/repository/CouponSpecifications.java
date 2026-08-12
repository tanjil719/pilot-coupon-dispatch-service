package com.pilotcoupondispatchservice.modules.coupons.repository;

import com.pilotcoupondispatchservice.enums.CouponStatus;
import com.pilotcoupondispatchservice.modules.coupons.entity.Coupon;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

public class CouponSpecifications {

    private CouponSpecifications() {
    }

    public static Specification<Coupon> ownerIdEquals(Long ownerId) {
        return (root, query, cb) -> (ownerId == null) ? cb.conjunction() : cb.equal(root.get("owner").get("id"), ownerId);
    }

    public static Specification<Coupon> statusEquals(CouponStatus status) {
        return (root, query, cb) -> (status == null) ? cb.conjunction() : cb.equal(root.get("status"), status);
    }

    public static Specification<Coupon> issuedAtFrom(LocalDateTime from) {
        return (root, query, cb) -> (from == null) ? cb.conjunction() : cb.greaterThanOrEqualTo(root.get("issuedAt"), from);
    }

    public static Specification<Coupon> issuedAtTo(LocalDateTime to) {
        return (root, query, cb) -> (to == null) ? cb.conjunction() : cb.lessThanOrEqualTo(root.get("issuedAt"), to);
    }

    public static Specification<Coupon> codeContains(String search) {
        return (root, query, cb) -> {
            if (!StringUtils.hasText(search)) {
                return cb.conjunction();
            }
            String pattern = "%" + search.trim().toLowerCase() + "%";
            return cb.like(cb.lower(root.get("code")), pattern);
        };
    }

    public static Specification<Coupon> fetchOwner() {
        return (root, query, cb) -> {
            if (query.getResultType() != Long.class && query.getResultType() != long.class) {
                root.fetch("owner", JoinType.LEFT);
            }
            return cb.conjunction();
        };
    }
}
