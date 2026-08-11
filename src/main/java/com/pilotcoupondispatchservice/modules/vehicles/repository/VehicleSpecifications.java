package com.pilotcoupondispatchservice.modules.vehicles.repository;

import com.pilotcoupondispatchservice.enums.VehicleStatus;
import com.pilotcoupondispatchservice.enums.VehicleType;
import com.pilotcoupondispatchservice.modules.vehicles.entity.Vehicle;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

public class VehicleSpecifications {

    private VehicleSpecifications() {
    }

    public static Specification<Vehicle> ownerIdEquals(Long ownerId) {
        return (root, query, cb) -> (ownerId == null) ? cb.conjunction() : cb.equal(root.get("owner").get("id"), ownerId);
    }

    public static Specification<Vehicle> activeIsTrue() {
        return (root, query, cb) -> cb.isTrue(root.get("active"));
    }

    public static Specification<Vehicle> activeEquals(Boolean active) {
        return (root, query, cb) -> (active == null) ? cb.conjunction() : cb.equal(root.get("active"), active);
    }

    public static Specification<Vehicle> statusEquals(VehicleStatus status) {
        return (root, query, cb) -> (status == null) ? cb.conjunction() : cb.equal(root.get("status"), status);
    }

    public static Specification<Vehicle> typeEquals(VehicleType type) {
        return (root, query, cb) -> (type == null) ? cb.conjunction() : cb.equal(root.get("type"), type);
    }

    public static Specification<Vehicle> nameOrRegistrationNoContains(String search) {
        return (root, query, cb) -> {
            if (!StringUtils.hasText(search)) {
                return cb.conjunction();
            }
            String pattern = "%" + search.trim().toLowerCase() + "%";
            return cb.or(
                    cb.like(cb.lower(root.get("name")), pattern),
                    cb.like(cb.lower(root.get("registrationNo")), pattern)
            );
        };
    }


    public static Specification<Vehicle> fetchOwnerAndApprovedBy() {
        return (root, query, cb) -> {
            if (query.getResultType() != Long.class && query.getResultType() != long.class) {
                root.fetch("owner", JoinType.LEFT);
                root.fetch("approvedBy", JoinType.LEFT);
            }
            return cb.conjunction();
        };
    }
}
