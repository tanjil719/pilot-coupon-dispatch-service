package com.pilotcoupondispatchservice.modules.routes.service;

import com.pilotcoupondispatchservice.modules.routes.dto.RouteAdminResponse;
import com.pilotcoupondispatchservice.modules.routes.dto.RouteRequest;
import com.pilotcoupondispatchservice.modules.routes.dto.RouteResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RouteService {

    // ---- Admin ----

    RouteAdminResponse createRoute(RouteRequest request);

    Page<RouteAdminResponse> listAllRoutes(Boolean active, String search, Double minFee, Double maxFee, Pageable pageable);

    RouteAdminResponse getRoute(Long id);

    RouteAdminResponse updateRoute(Long id, RouteRequest request);

    RouteAdminResponse updateRouteStatus(Long id, Boolean active);


    // ---- Owner ----

    Page<RouteResponse> listActiveRoutes(String search, Double minFee, Double maxFee, Pageable pageable);

    RouteResponse getActiveRouteById(Long id);
}
