package com.pilotcoupondispatchservice.modules.bookings.repository;

import com.pilotcoupondispatchservice.enums.BookingStatus;
import com.pilotcoupondispatchservice.modules.bookings.entity.Booking;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

public class BookingSpecifications {

    private BookingSpecifications() {
    }

    public static Specification<Booking> ownerIdEquals(Long ownerId) {
        return (root, query, cb) -> (ownerId == null) ? cb.conjunction() : cb.equal(root.get("owner").get("id"), ownerId);
    }

    public static Specification<Booking> statusEquals(BookingStatus status) {
        return (root, query, cb) -> (status == null) ? cb.conjunction() : cb.equal(root.get("status"), status);
    }

    public static Specification<Booking> vehicleRegistrationNoContains(String registrationNo) {
        return (root, query, cb) -> {
            if (!StringUtils.hasText(registrationNo)) {
                return cb.conjunction();
            }
            return cb.like(cb.lower(root.get("vehicle").get("registrationNo")), "%" + registrationNo.trim().toLowerCase() + "%");
        };
    }

    public static Specification<Booking> ownerNameContains(String ownerName) {
        return (root, query, cb) -> {
            if (!StringUtils.hasText(ownerName)) {
                return cb.conjunction();
            }
            return cb.like(cb.lower(root.get("owner").get("name")), "%" + ownerName.trim().toLowerCase() + "%");
        };
    }

    public static Specification<Booking> routeCodeContains(String routeCode) {
        return (root, query, cb) -> {
            if (!StringUtils.hasText(routeCode)) {
                return cb.conjunction();
            }
            return cb.like(cb.lower(root.get("route").get("routeCode")), "%" + routeCode.trim().toLowerCase() + "%");
        };
    }

    public static Specification<Booking> serviceStartFrom(LocalDateTime from) {
        return (root, query, cb) -> (from == null) ? cb.conjunction() : cb.greaterThanOrEqualTo(root.get("serviceStart"), from);
    }

    public static Specification<Booking> serviceStartTo(LocalDateTime to) {
        return (root, query, cb) -> (to == null) ? cb.conjunction() : cb.lessThanOrEqualTo(root.get("serviceStart"), to);
    }

    public static Specification<Booking> fetchAssociations() {
        return (root, query, cb) -> {
            if (query.getResultType() != Long.class && query.getResultType() != long.class) {
                root.fetch("owner", JoinType.LEFT);
                root.fetch("vehicle", JoinType.LEFT);
                root.fetch("route", JoinType.LEFT);
                root.fetch("coupon", JoinType.LEFT);
                root.fetch("pilot", JoinType.LEFT);
            }
            return cb.conjunction();
        };
    }
}
