package com.pilotcoupondispatchservice.modules.routes.service;

import com.pilotcoupondispatchservice.annotations.HasPermission;
import com.pilotcoupondispatchservice.constants.PermissionConstant;
import com.pilotcoupondispatchservice.exceptions.InvalidRequestException;
import com.pilotcoupondispatchservice.exceptions.ResourceAlreadyExistException;
import com.pilotcoupondispatchservice.exceptions.ResourceNotFoundException;
import com.pilotcoupondispatchservice.modules.routes.dto.RouteAdminResponse;
import com.pilotcoupondispatchservice.modules.routes.dto.RouteRequest;
import com.pilotcoupondispatchservice.modules.routes.dto.RouteResponse;
import com.pilotcoupondispatchservice.modules.routes.entity.Route;
import com.pilotcoupondispatchservice.modules.routes.mapper.RouteMapper;
import com.pilotcoupondispatchservice.modules.routes.repository.RouteRepository;
import com.pilotcoupondispatchservice.modules.routes.repository.RouteSpecifications;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.data.jpa.domain.Specification.where;

@Service
@AllArgsConstructor
public class RouteServiceImpl implements RouteService {

    private final RouteRepository routeRepository;

    @Override
    @HasPermission(permission = PermissionConstant.ROUTE_CREATE)
    public RouteAdminResponse createRoute(RouteRequest request) {
        String startPoint = request.getStartPoint().trim();
        String endPoint = request.getEndPoint().trim();

        validateDistinctPoints(startPoint, endPoint);
        validateUniquePath(startPoint, endPoint, null);

        String routeCode = normalizeRouteCode(request.getRouteCode());
        if (routeRepository.existsByRouteCodeIgnoreCase(routeCode)) {
            throw new ResourceAlreadyExistException("Route already exists with route code: '" + routeCode + "'");
        }

        Route route = new Route();
        route.setRouteCode(routeCode);
        route.setName(request.getName());
        route.setStartPoint(startPoint);
        route.setEndPoint(endPoint);
        route.setDistanceKm(request.getDistanceKm());
        route.setEstimatedDurationMinutes(request.getEstimatedDurationMinutes());
        route.setServiceFee(request.getServiceFee());
        route.setDescription(request.getDescription());
        route.setActive(true);

        return RouteMapper.toAdminResponse(routeRepository.save(route));
    }

    @Override
    @HasPermission(permission = PermissionConstant.ROUTE_VIEW_ALL)
    public Page<RouteAdminResponse> listAllRoutes(Boolean active, String search, Double minFee, Double maxFee, Pageable pageable) {
        Specification<Route> specification = where(RouteSpecifications.activeEquals(active))
                .and(RouteSpecifications.searchContains(search))
                .and(RouteSpecifications.feeGreaterThanOrEqual(minFee))
                .and(RouteSpecifications.feeLessThanOrEqual(maxFee));

        return routeRepository.findAll(specification, pageable).map(RouteMapper::toAdminResponse);
    }

    @Override
    @HasPermission(permission = PermissionConstant.ROUTE_VIEW_ALL)
    public RouteAdminResponse getRoute(Long id) {
        return RouteMapper.toAdminResponse(findByIdOrThrow(id));
    }

    @Override
    @HasPermission(permission = PermissionConstant.ROUTE_UPDATE)
    public RouteAdminResponse updateRoute(Long id, RouteRequest request) {
        Route route = findByIdOrThrow(id);

        String startPoint = request.getStartPoint().trim();
        String endPoint = request.getEndPoint().trim();

        validateDistinctPoints(startPoint, endPoint);
        validateUniquePath(startPoint, endPoint, id);


        String routeCode = normalizeRouteCode(request.getRouteCode());
        if (!routeCode.equalsIgnoreCase(route.getRouteCode())
                && routeRepository.existsByRouteCodeIgnoreCaseAndIdNot(routeCode, id)) {
            throw new ResourceAlreadyExistException("Route already exists with route code: '" + routeCode + "'");
        }
        route.setRouteCode(routeCode);


        route.setName(request.getName());
        route.setStartPoint(startPoint);
        route.setEndPoint(endPoint);
        route.setDistanceKm(request.getDistanceKm());
        route.setEstimatedDurationMinutes(request.getEstimatedDurationMinutes());

        // TODO(next-module): changing serviceFee here only affects future bookings. Booking copies
        // feeAmount onto itself at creation time, so no historical recalculation is ever performed
        // for bookings that already exist on this route.
        route.setServiceFee(request.getServiceFee());
        route.setDescription(request.getDescription());

        return RouteMapper.toAdminResponse(routeRepository.save(route));
    }

    @Override
    @HasPermission(permission = PermissionConstant.ROUTE_CHANGE_STATUS)
    public RouteAdminResponse updateRouteStatus(Long id, Boolean active) {
        Route route = findByIdOrThrow(id);

        // TODO(next-module): deactivating a route does not cancel anything. Already-approved
        // bookings on this route continue normally; only NEW bookings are blocked, once Booking
        // creation checks route.active (see the ROUTE_INACTIVE TODO on getActiveRoute below).
        route.setActive(active);

        return RouteMapper.toAdminResponse(routeRepository.save(route));
    }

    @Override
    @HasPermission(permission = PermissionConstant.ROUTE_VIEW)
    public Page<RouteResponse> listActiveRoutes(String search, Double minFee, Double maxFee, Pageable pageable) {
        Specification<Route> specification = where(RouteSpecifications.activeIsTrue())
                .and(RouteSpecifications.searchContains(search))
                .and(RouteSpecifications.feeGreaterThanOrEqual(minFee))
                .and(RouteSpecifications.feeLessThanOrEqual(maxFee));

        return routeRepository.findAll(specification, pageable).map(RouteMapper::toResponse);
    }

    @Override
    @HasPermission(permission = PermissionConstant.ROUTE_VIEW)
    @Transactional(readOnly = true)
    public RouteResponse getActiveRouteById(Long id) {
        Route route = routeRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Route not found with id: '" + id + "'"));

        // TODO(next-module): Booking creation must re-check the route is still active at submission
        // time, since a route can be deactivated between browsing and submitting. Call something
        // like this from BookingServiceImpl#createBooking before creating the booking:
        //
        // private void assertRouteIsActive(Long routeId) {
        //     Route route = routeRepository.findById(routeId)
        //             .orElseThrow(() -> new ResourceNotFoundException("ROUTE_NOT_FOUND: Route not found with id: '" + routeId + "'"));
        //     if (!route.getActive()) {
        //         throw new InvalidRequestException("ROUTE_INACTIVE: Route is no longer active and cannot accept new bookings");
        //     }
        // }

        return RouteMapper.toResponse(route);
    }

    //*********** Internal Helper Methods ***********//

    private Route findByIdOrThrow(Long id) {
        return routeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(" Route not found with id: '" + id + "'"));
    }

    private void validateDistinctPoints(String startPoint, String endPoint) {
        if (startPoint.equalsIgnoreCase(endPoint)) {
            throw new InvalidRequestException("Start point and end point must not be the same");
        }
    }

    private void validateUniquePath(String startPoint, String endPoint, Long excludeId) {
        boolean exists = (excludeId == null)
                ? routeRepository.existsByStartPointIgnoreCaseAndEndPointIgnoreCase(startPoint, endPoint)
                : routeRepository.existsByStartPointIgnoreCaseAndEndPointIgnoreCaseAndIdNot(startPoint, endPoint, excludeId);

        if (exists) {
            throw new ResourceAlreadyExistException("A route between '" + startPoint + "' and '" + endPoint + "' already exists");
        }
    }


    private String normalizeRouteCode(String routeCode) {
        return routeCode.trim().toUpperCase();
    }
}
