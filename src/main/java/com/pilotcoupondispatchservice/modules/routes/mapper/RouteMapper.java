package com.pilotcoupondispatchservice.modules.routes.mapper;

import com.pilotcoupondispatchservice.modules.routes.dto.RouteAdminResponse;
import com.pilotcoupondispatchservice.modules.routes.dto.RouteResponse;
import com.pilotcoupondispatchservice.modules.routes.entity.Route;
import com.pilotcoupondispatchservice.modules.users.entity.User;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class RouteMapper {

    private RouteMapper() {
    }

    public static RouteResponse toResponse(Route route) {
        RouteResponse response = new RouteResponse();
        response.setId(route.getId());
        response.setRouteCode(route.getRouteCode());
        response.setName(route.getName());
        response.setStartPoint(route.getStartPoint());
        response.setEndPoint(route.getEndPoint());
        response.setDistanceKm(route.getDistanceKm());
        response.setEstimatedDurationMinutes(route.getEstimatedDurationMinutes());
        response.setServiceFee(route.getServiceFee());
        response.setDescription(route.getDescription());
        return response;
    }

    public static RouteAdminResponse toAdminResponse(Route route) {
        RouteAdminResponse response = new RouteAdminResponse();
        response.setId(route.getId());
        response.setRouteCode(route.getRouteCode());
        response.setName(route.getName());
        response.setStartPoint(route.getStartPoint());
        response.setEndPoint(route.getEndPoint());
        response.setDistanceKm(route.getDistanceKm());
        response.setEstimatedDurationMinutes(route.getEstimatedDurationMinutes());
        response.setServiceFee(route.getServiceFee());
        response.setDescription(route.getDescription());
        response.setActive(Boolean.TRUE.equals(route.getActive()));

        response.setCreatedByName(route.getCreatedBy());
        response.setUpdatedByName(route.getUpdatedBy());

        response.setCreatedAt(route.getCreatedAt());
        response.setUpdatedAt(route.getUpdatedAt());
        return response;
    }

//    private static String formatFee(BigDecimal serviceFee) {
//        return serviceFee == null ? null : serviceFee.setScale(2, RoundingMode.HALF_UP).toPlainString();
//    }
}
