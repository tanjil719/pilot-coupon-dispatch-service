package com.pilotcoupondispatchservice.modules.vehicles.controller;

import com.pilotcoupondispatchservice.enums.VehicleStatus;
import com.pilotcoupondispatchservice.enums.VehicleType;
import com.pilotcoupondispatchservice.modules.vehicles.dto.VehicleAdminResponse;
import com.pilotcoupondispatchservice.modules.vehicles.dto.VehicleReviewRequest;
import com.pilotcoupondispatchservice.modules.vehicles.service.VehicleService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${url.base}/secured/admin/vehicles")
@AllArgsConstructor
public class AdminVehicleController {

    private final VehicleService vehicleService;

    @GetMapping
    public ResponseEntity<?> listAllVehicles(
            @RequestParam(required = false) VehicleStatus status,
            @RequestParam(required = false) VehicleType type,
            @RequestParam(required = false) Long ownerId,
            @RequestParam(defaultValue = "") String search,
            @RequestParam(required = false) Boolean active,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") Sort.Direction sortDir) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDir, sortBy));
        Page<VehicleAdminResponse> vehicles = vehicleService.listAllVehicles(status, type, ownerId, search, active, pageable);
        return ResponseEntity.ok(vehicles);
    }

    @GetMapping("/by-id/{id}")
    public ResponseEntity<?> getVehicle(@PathVariable Long id) {
        VehicleAdminResponse response = vehicleService.getVehicle(id);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/review/{id}")
    public ResponseEntity<?> reviewVehicle(@PathVariable Long id, @Valid @RequestBody VehicleReviewRequest request) {
        VehicleAdminResponse response = vehicleService.reviewVehicle(id, request);
        return ResponseEntity.ok(response);
    }
}
