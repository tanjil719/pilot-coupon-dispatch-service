package com.pilotcoupondispatchservice.modules.routes.controller;

import com.pilotcoupondispatchservice.modules.routes.dto.RouteResponse;
import com.pilotcoupondispatchservice.modules.routes.service.RouteService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${url.base}/secured/owner/routes")
@AllArgsConstructor
public class OwnerRouteController {

    private final RouteService routeService;

    @GetMapping
    public ResponseEntity<?> listActiveRoutes(
            @RequestParam(defaultValue = "") String search,
            @RequestParam(required = false) Double minFee,
            @RequestParam(required = false) Double maxFee,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") Sort.Direction sortDir) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDir, sortBy));
        Page<RouteResponse> routes = routeService.listActiveRoutes(search, minFee, maxFee, pageable);
        return ResponseEntity.ok(routes);
    }

    @GetMapping("/by-id/{id}")
    public ResponseEntity<?> getActiveRouteById(@PathVariable Long id) {
        RouteResponse response = routeService.getActiveRouteById(id);
        return ResponseEntity.ok(response);
    }
}
