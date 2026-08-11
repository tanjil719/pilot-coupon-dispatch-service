package com.pilotcoupondispatchservice.modules.vehicles.service;

import com.pilotcoupondispatchservice.annotations.HasPermission;
import com.pilotcoupondispatchservice.constants.PermissionConstant;
import com.pilotcoupondispatchservice.enums.VehicleStatus;
import com.pilotcoupondispatchservice.enums.VehicleType;
import com.pilotcoupondispatchservice.exceptions.InvalidRequestException;
import com.pilotcoupondispatchservice.exceptions.ResourceAlreadyExistException;
import com.pilotcoupondispatchservice.exceptions.ResourceNotFoundException;
import com.pilotcoupondispatchservice.modules.users.entity.User;
import com.pilotcoupondispatchservice.modules.users.repository.UserRepository;
import com.pilotcoupondispatchservice.modules.vehicles.dto.*;
import com.pilotcoupondispatchservice.modules.vehicles.entity.Vehicle;
import com.pilotcoupondispatchservice.modules.vehicles.mapper.VehicleMapper;
import com.pilotcoupondispatchservice.modules.vehicles.repository.VehicleRepository;
import com.pilotcoupondispatchservice.modules.vehicles.repository.VehicleSpecifications;
import com.pilotcoupondispatchservice.utils.SecurityUtil;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.springframework.data.jpa.domain.Specification.where;

@Service
@AllArgsConstructor
public class VehicleServiceImpl implements VehicleService {

    private final VehicleRepository vehicleRepository;
    private final UserRepository userRepository;

    @Override
    @HasPermission(permission = PermissionConstant.VEHICLE_CREATE)
    public VehicleResponse createVehicle(VehicleRequest request) {
        String registrationNo = normalizeRegistrationNo(request.getRegistrationNo());

        if (vehicleRepository.existsByRegistrationNoIgnoreCase(registrationNo)) {
            throw new ResourceAlreadyExistException("Vehicle already exists with registration no: '" + registrationNo + "'");
        }

        Long ownerId = SecurityUtil.getLoggedInUserId();

        User owner = userRepository.findById(ownerId).orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + ownerId));
        if (!owner.getIsActive()) {
            throw new InvalidRequestException("Cannot create vehicle for an inactive user");
        }

        Vehicle vehicle = new Vehicle();
        vehicle.setOwner(owner);
        vehicle.setName(request.getName());
        vehicle.setRegistrationNo(registrationNo);
        vehicle.setType(request.getType());
        vehicle.setCapacity(request.getCapacity());
        vehicle.setDescription(request.getDescription());
        vehicle.setStatus(VehicleStatus.PENDING);
        vehicle.setActive(true);

        return VehicleMapper.toResponse(vehicleRepository.save(vehicle));
    }

    @Override
    @HasPermission(permission = PermissionConstant.VEHICLE_VIEW_OWN)
    public Page<VehicleResponse> listOwnVehicles(VehicleStatus status, VehicleType type, String search, Pageable pageable) {
        Long ownerId = SecurityUtil.getLoggedInUserId();

        Specification<Vehicle> specification = where(VehicleSpecifications.ownerIdEquals(ownerId))
                .and(VehicleSpecifications.activeIsTrue())
                .and(VehicleSpecifications.statusEquals(status))
                .and(VehicleSpecifications.typeEquals(type))
                .and(VehicleSpecifications.nameOrRegistrationNoContains(search));

        return vehicleRepository.findAll(specification, pageable).map(VehicleMapper::toResponse);
    }

    @Override
    @HasPermission(permission = PermissionConstant.VEHICLE_VIEW_OWN)
    public VehicleResponse getOwnVehicle(Long id) {
        return VehicleMapper.toResponse(findByIdAndOwnerId(id));
    }

    @Override
    @HasPermission(permission = PermissionConstant.VEHICLE_UPDATE)
    public VehicleResponse updateVehicle(Long id, VehicleRequest request) {
        Vehicle vehicle = findByIdAndOwnerId(id);

        if (!vehicle.getActive()) {
            throw new InvalidRequestException("Vehicle has been deleted and can no longer be updated");
        }

        String registrationNo = normalizeRegistrationNo(request.getRegistrationNo());
        if (!registrationNo.equalsIgnoreCase(vehicle.getRegistrationNo())
                && vehicleRepository.existsByRegistrationNoIgnoreCaseAndIdNot(registrationNo, id)) {
            throw new ResourceAlreadyExistException("Vehicle already exists with registration no: '" + registrationNo + "'");
        }

        vehicle.setName(request.getName());
        vehicle.setRegistrationNo(registrationNo);
        vehicle.setType(request.getType());
        vehicle.setCapacity(request.getCapacity());
        vehicle.setDescription(request.getDescription());

        // Owner rule: any update sends the vehicle back for re-review, whatever its prior status.
        vehicle.setStatus(VehicleStatus.PENDING);
//        vehicle.setRejectionReason(null);
//        vehicle.setApprovedBy(null);
//        vehicle.setReviewedAt(null);

        return VehicleMapper.toResponse(vehicleRepository.save(vehicle));
    }

    @Override
    @HasPermission(permission = PermissionConstant.VEHICLE_DELETE)
    public boolean deleteVehicle(Long id) {
        Vehicle vehicle = findByIdAndOwnerId(id);

        if (!vehicle.getActive()) {
            return false;
        }

        // TODO(next-module): block soft delete while an active booking references this vehicle.
        // if (bookingRepository.existsByVehicleIdAndStatusIn(vehicle.getId(), Booking.ACTIVE_STATUSES)) {
        //     throw new InvalidRequestException("VEHICLE_IN_USE: Cannot delete vehicle referenced by an active booking");
        // }

        vehicle.setActive(false);
        vehicleRepository.save(vehicle);
        return true;
    }

    @Override
    @HasPermission(permission = PermissionConstant.VEHICLE_VIEW_ALL)
    @Transactional(readOnly = true)
    public Page<VehicleAdminResponse> listAllVehicles(VehicleStatus status, VehicleType type, Long ownerId, String search, Boolean active, Pageable pageable) {

        Specification<Vehicle> specification = where(VehicleSpecifications.activeEquals(active))
                .and(VehicleSpecifications.statusEquals(status))
                .and(VehicleSpecifications.typeEquals(type))
                .and(VehicleSpecifications.ownerIdEquals(ownerId))
                .and(VehicleSpecifications.nameOrRegistrationNoContains(search))
                .and(VehicleSpecifications.fetchOwnerAndApprovedBy());

        return vehicleRepository.findAll(specification, pageable).map(VehicleMapper::toAdminResponse);
    }

    @Override
    @HasPermission(permission = PermissionConstant.VEHICLE_VIEW_ALL)
    public VehicleAdminResponse getVehicle(Long id) {
        return VehicleMapper.toAdminResponse(findByIdOrThrow(id));
    }

    @Override
    @HasPermission(permission = PermissionConstant.VEHICLE_REVIEW)
    public VehicleAdminResponse reviewVehicle(Long id, VehicleReviewRequest request) {
        Vehicle vehicle = findByIdOrThrow(id);

        if (!vehicle.getActive()){
            throw new InvalidRequestException("Cannot review a deleted vehicle");
        }

        if (vehicle.getStatus() != VehicleStatus.PENDING) {
            throw new InvalidRequestException("Only a PENDING vehicle can be reviewed, current status is " + vehicle.getStatus());
        }

        if (request.getStatus() == VehicleStatus.APPROVED) {
            vehicle.setStatus(VehicleStatus.APPROVED);
            vehicle.setRejectionReason(null);
        } else if (request.getStatus() == VehicleStatus.REJECTED) {
            if (request.getReason() == null || request.getReason().isBlank()) {
                throw new InvalidRequestException("Reason is required to reject a vehicle");
            }
            vehicle.setStatus(VehicleStatus.REJECTED);
            vehicle.setRejectionReason(request.getReason());
        } else {
            throw new InvalidRequestException("Status must be either APPROVED or REJECTED");
        }

        vehicle.setApprovedBy(
                userRepository.findById(SecurityUtil.getLoggedInUserId())
                        .orElseThrow(() -> new ResourceNotFoundException(
                                "User not found"
                        ))
        );
        vehicle.setReviewedAt(LocalDateTime.now());

        return VehicleMapper.toAdminResponse(vehicleRepository.save(vehicle));
    }

    //*********** Internal Helper Methods ***********//

    private Vehicle findByIdAndOwnerId(Long id) {
        Long ownerId = SecurityUtil.getLoggedInUserId();
        return vehicleRepository.findByIdAndOwnerId(id, ownerId)
                .orElseThrow(() -> new ResourceNotFoundException("VEHICLE_NOT_FOUND: Vehicle not found with id: '" + id + "'"));
    }

    private Vehicle findByIdOrThrow(Long id) {
        return vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("VEHICLE_NOT_FOUND: Vehicle not found with id: '" + id + "'"));
    }

    private String normalizeRegistrationNo(String registrationNo) {
        return registrationNo.trim().toUpperCase();
    }
}
