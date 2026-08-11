package com.pilotcoupondispatchservice.modules.routes.controller;

import com.pilotcoupondispatchservice.modules.routes.dto.RouteAdminResponse;
import com.pilotcoupondispatchservice.modules.routes.dto.RouteRequest;
import com.pilotcoupondispatchservice.modules.routes.service.RouteService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("${url.base}/secured/admin/routes")
@AllArgsConstructor
public class AdminRouteController {

    private final RouteService routeService;

    @PostMapping("/create")
    public ResponseEntity<?> createRoute(@Valid @RequestBody RouteRequest request) {
        RouteAdminResponse response = routeService.createRoute(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<?> listAllRoutes(
            @RequestParam(required = false) Boolean active,
            @RequestParam(defaultValue = "") String search,
            @RequestParam(required = false) Double minFee,
            @RequestParam(required = false) Double maxFee,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") Sort.Direction sortDir) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDir, sortBy));
        Page<RouteAdminResponse> routes = routeService.listAllRoutes(active, search, minFee, maxFee, pageable);
        return ResponseEntity.ok(routes);
    }

    @GetMapping("/by-id/{id}")
    public ResponseEntity<?> getRoute(@PathVariable Long id) {
        RouteAdminResponse response = routeService.getRoute(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateRoute(@PathVariable Long id, @Valid @RequestBody RouteRequest request) {
        RouteAdminResponse response = routeService.updateRoute(id, request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/status/{id}")
    public ResponseEntity<?> updateRouteStatus(@PathVariable Long id, @RequestParam Boolean active) {
        RouteAdminResponse response = routeService.updateRouteStatus(id, active);
        return ResponseEntity.ok(response);
    }
}
