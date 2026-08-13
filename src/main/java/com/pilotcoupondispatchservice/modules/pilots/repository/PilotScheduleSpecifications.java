package com.pilotcoupondispatchservice.modules.pilots.repository;

import com.pilotcoupondispatchservice.enums.BookingStatus;
import com.pilotcoupondispatchservice.enums.ScheduleStatus;
import com.pilotcoupondispatchservice.modules.bookings.entity.Booking;
import com.pilotcoupondispatchservice.modules.pilots.entity.PilotSchedule;
import jakarta.persistence.criteria.Fetch;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

public class PilotScheduleSpecifications {

    private PilotScheduleSpecifications() {
    }

    public static Specification<PilotSchedule> pilotIdEquals(Long pilotId) {
        return (root, query, cb) -> (pilotId == null) ? cb.conjunction() : cb.equal(root.get("pilot").get("id"), pilotId);
    }

    public static Specification<PilotSchedule> bookingIdEquals(Long bookingId) {
        return (root, query, cb) -> (bookingId == null) ? cb.conjunction() : cb.equal(root.get("booking").get("id"), bookingId);
    }

    public static Specification<PilotSchedule> routeCodeEquals(String routeCode) {
        return (root, query, cb) -> (!StringUtils.hasText(routeCode)) ? cb.conjunction() : cb.equal(cb.lower(root.get("routeCode")), routeCode.trim().toLowerCase());
    }

    public static Specification<PilotSchedule> statusEquals(ScheduleStatus status) {
        return (root, query, cb) -> {
            if (status == null) {
                return cb.conjunction();
            }
            LocalDateTime now = LocalDateTime.now();
            return switch (status) {
                case SCHEDULED -> cb.greaterThan(root.get("serviceStart"), now);
                case IN_PROGRESS -> cb.and(
                        cb.lessThanOrEqualTo(root.get("serviceStart"), now),
                        cb.greaterThan(root.get("serviceEnd"), now));
                case COMPLETED -> cb.lessThanOrEqualTo(root.get("serviceEnd"), now);
            };
        };
    }

    public static Specification<PilotSchedule> statusEquals(BookingStatus status) {
        return (root, query, cb) -> (status == null) ? cb.conjunction() : cb.equal(root.get("booking").get("status"), status);
    }

    public static Specification<PilotSchedule> serviceStartFrom(LocalDateTime from) {
        return (root, query, cb) -> (from == null) ? cb.conjunction() : cb.greaterThanOrEqualTo(root.get("serviceStart"), from);
    }

    public static Specification<PilotSchedule> serviceStartTo(LocalDateTime to) {
        return (root, query, cb) -> (to == null) ? cb.conjunction() : cb.lessThanOrEqualTo(root.get("serviceStart"), to);
    }

    public static Specification<PilotSchedule> fetchAssociations() {
        return (root, query, cb) -> {
            if (query.getResultType() != Long.class && query.getResultType() != long.class) {
                root.fetch("pilot", JoinType.LEFT);
                Fetch<PilotSchedule, Booking> bookingFetch = root.fetch("booking", JoinType.LEFT);
                bookingFetch.fetch("vehicle", JoinType.LEFT);
            }
            return cb.conjunction();
        };
    }
}
