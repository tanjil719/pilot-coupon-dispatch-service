package com.pilotcoupondispatchservice.modules.bookings.repository;

import com.pilotcoupondispatchservice.enums.BookingStatus;
import com.pilotcoupondispatchservice.modules.bookings.entity.Booking;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

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

    public static Specification<Booking> vehicleIdEquals(Long vehicleId) {
        return (root, query, cb) -> (vehicleId == null) ? cb.conjunction() : cb.equal(root.get("vehicle").get("id"), vehicleId);
    }

    public static Specification<Booking> routeIdEquals(Long routeId) {
        return (root, query, cb) -> (routeId == null) ? cb.conjunction() : cb.equal(root.get("route").get("id"), routeId);
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
            }
            return cb.conjunction();
        };
    }
}
