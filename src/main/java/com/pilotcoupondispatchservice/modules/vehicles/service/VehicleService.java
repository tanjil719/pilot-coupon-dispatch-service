package com.pilotcoupondispatchservice.modules.vehicles.service;

import com.pilotcoupondispatchservice.enums.VehicleStatus;
import com.pilotcoupondispatchservice.enums.VehicleType;
import com.pilotcoupondispatchservice.modules.vehicles.dto.VehicleAdminResponse;
import com.pilotcoupondispatchservice.modules.vehicles.dto.VehicleRequest;
import com.pilotcoupondispatchservice.modules.vehicles.dto.VehicleResponse;
import com.pilotcoupondispatchservice.modules.vehicles.dto.VehicleReviewRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface VehicleService {

    // ---- Owner ----

    VehicleResponse createVehicle(VehicleRequest request);

    Page<VehicleResponse> listOwnVehicles(VehicleStatus status, VehicleType type, String search, Pageable pageable);

    VehicleResponse getOwnVehicle(Long id);

    VehicleResponse updateVehicle(Long id, VehicleRequest request);

    boolean deleteVehicle(Long id);

    // ---- Admin ----

    Page<VehicleAdminResponse> listAllVehicles(VehicleStatus status, VehicleType type, Long ownerId, String search, Boolean active, Pageable pageable);

    VehicleAdminResponse getVehicle(Long id);

    VehicleAdminResponse reviewVehicle(Long id, VehicleReviewRequest request);
}
