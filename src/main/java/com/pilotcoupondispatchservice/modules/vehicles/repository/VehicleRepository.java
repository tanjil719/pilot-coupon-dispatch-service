package com.pilotcoupondispatchservice.modules.vehicles.repository;

import com.pilotcoupondispatchservice.modules.vehicles.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long>, JpaSpecificationExecutor<Vehicle> {

    Optional<Vehicle> findByIdAndOwnerId(Long id, Long ownerId);

    @Query("SELECT v FROM Vehicle v LEFT JOIN FETCH v.owner LEFT JOIN FETCH v.approvedBy WHERE v.id = :id")
    Optional<Vehicle> findByIdWithOwnerAndApprovedBy(@Param("id") Long id);

    boolean existsByRegistrationNoIgnoreCase(String registrationNo);

    boolean existsByRegistrationNoIgnoreCaseAndIdNot(String registrationNo, Long id);
}
