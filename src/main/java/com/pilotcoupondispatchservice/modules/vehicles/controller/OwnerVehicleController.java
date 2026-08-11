package com.pilotcoupondispatchservice.modules.vehicles.controller;

import com.pilotcoupondispatchservice.enums.VehicleStatus;
import com.pilotcoupondispatchservice.enums.VehicleType;
import com.pilotcoupondispatchservice.modules.vehicles.dto.VehicleRequest;
import com.pilotcoupondispatchservice.modules.vehicles.dto.VehicleResponse;
import com.pilotcoupondispatchservice.modules.vehicles.service.VehicleService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${url.base}/secured/owner/vehicles")
@AllArgsConstructor
public class OwnerVehicleController {

    private final VehicleService vehicleService;

    @PostMapping("/create")
    public ResponseEntity<?> createVehicle(@Valid @RequestBody VehicleRequest request) {
        VehicleResponse response = vehicleService.createVehicle(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<?> listOwnVehicles(
            @RequestParam(required = false) VehicleStatus status,
            @RequestParam(required = false) VehicleType type,
            @RequestParam(defaultValue = "") String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") Sort.Direction sortDir) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDir, sortBy));
        Page<VehicleResponse> vehicles = vehicleService.listOwnVehicles(status, type, search, pageable);
        return ResponseEntity.ok(vehicles);
    }

    @GetMapping("/by-id/{id}")
    public ResponseEntity<?> getOwnVehicle(@PathVariable Long id) {
        VehicleResponse response = vehicleService.getOwnVehicle(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateVehicle(@PathVariable Long id, @Valid @RequestBody VehicleRequest request) {
        VehicleResponse response = vehicleService.updateVehicle(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteVehicle(@PathVariable Long id) {
        boolean deleted = vehicleService.deleteVehicle(id);
        return ResponseEntity.ok(deleted);
    }
}
