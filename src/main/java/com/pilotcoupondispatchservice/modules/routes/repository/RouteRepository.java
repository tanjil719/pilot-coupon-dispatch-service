package com.pilotcoupondispatchservice.modules.routes.repository;

import com.pilotcoupondispatchservice.modules.routes.entity.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RouteRepository extends JpaRepository<Route, Long>, JpaSpecificationExecutor<Route> {

    Optional<Route> findByIdAndActiveTrue(Long id);

    @Query("SELECT r FROM Route r LEFT JOIN FETCH r.createdBy LEFT JOIN FETCH r.updatedBy WHERE r.id = :id")
    Optional<Route> findByIdWithAudit(@Param("id") Long id);

    boolean existsByRouteCodeIgnoreCase(String routeCode);

    boolean existsByRouteCodeIgnoreCaseAndIdNot(String routeCode, Long id);

    boolean existsByStartPointIgnoreCaseAndEndPointIgnoreCase(String startPoint, String endPoint);

    boolean existsByStartPointIgnoreCaseAndEndPointIgnoreCaseAndIdNot(String startPoint, String endPoint, Long id);
}
