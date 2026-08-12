package com.pilotcoupondispatchservice.modules.pilots.repository;

import com.pilotcoupondispatchservice.enums.PilotStatus;
import com.pilotcoupondispatchservice.modules.pilots.entity.Pilot;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

public class PilotSpecifications {

    private PilotSpecifications() {
    }

    public static Specification<Pilot> statusEquals(PilotStatus status) {
        return (root, query, cb) -> (status == null) ? cb.conjunction() : cb.equal(root.get("status"), status);
    }

    public static Specification<Pilot> searchContains(String search) {
        return (root, query, cb) -> {
            if (!StringUtils.hasText(search)) {
                return cb.conjunction();
            }
            String pattern = "%" + search.trim().toLowerCase() + "%";
            return cb.or(
                    cb.like(cb.lower(root.get("pilotCode")), pattern),
                    cb.like(cb.lower(root.get("name")), pattern),
                    cb.like(cb.lower(root.get("licenseNo")), pattern),
                    cb.like(cb.lower(root.get("phone")), pattern)
            );
        };
    }
}
