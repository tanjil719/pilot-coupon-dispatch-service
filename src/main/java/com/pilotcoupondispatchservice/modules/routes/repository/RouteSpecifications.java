package com.pilotcoupondispatchservice.modules.routes.repository;

import com.pilotcoupondispatchservice.modules.routes.entity.Route;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;

public class RouteSpecifications {

    private RouteSpecifications() {
    }

    public static Specification<Route> activeIsTrue() {
        return (root, query, cb) -> cb.isTrue(root.get("active"));
    }

    public static Specification<Route> activeEquals(Boolean active) {
        return (root, query, cb) -> (active == null) ? cb.conjunction() : cb.equal(root.get("active"), active);
    }

    public static Specification<Route> feeGreaterThanOrEqual(Double minFee) {
        return (root, query, cb) -> (minFee == null) ? cb.conjunction() : cb.greaterThanOrEqualTo(root.get("serviceFee"), minFee);
    }

    public static Specification<Route> feeLessThanOrEqual(Double maxFee) {
        return (root, query, cb) -> (maxFee == null) ? cb.conjunction() : cb.lessThanOrEqualTo(root.get("serviceFee"), maxFee);
    }

    public static Specification<Route> searchContains(String search) {
        return (root, query, cb) -> {
            if (!StringUtils.hasText(search)) {
                return cb.conjunction();
            }
            String pattern = "%" + search.trim().toLowerCase() + "%";
            return cb.or(
                    cb.like(cb.lower(root.get("routeCode")), pattern),
                    cb.like(cb.lower(root.get("name")), pattern),
                    cb.like(cb.lower(root.get("startPoint")), pattern),
                    cb.like(cb.lower(root.get("endPoint")), pattern)
            );
        };
    }

    public static Specification<Route> fetchCreatedByAndUpdatedBy() {
        return (root, query, cb) -> {
            if (query.getResultType() != Long.class && query.getResultType() != long.class) {
                root.fetch("createdBy", JoinType.LEFT);
                root.fetch("updatedBy", JoinType.LEFT);
            }
            return cb.conjunction();
        };
    }
}
