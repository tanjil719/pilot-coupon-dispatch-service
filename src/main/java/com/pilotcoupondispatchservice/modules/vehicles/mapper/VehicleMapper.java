package com.pilotcoupondispatchservice.modules.vehicles.mapper;

import com.pilotcoupondispatchservice.enums.VehicleStatus;
import com.pilotcoupondispatchservice.modules.users.entity.User;
import com.pilotcoupondispatchservice.modules.vehicles.dto.VehicleAdminResponse;
import com.pilotcoupondispatchservice.modules.vehicles.dto.VehicleResponse;
import com.pilotcoupondispatchservice.modules.vehicles.entity.Vehicle;

public final class VehicleMapper {

    private VehicleMapper() {
    }

    public static VehicleResponse toResponse(Vehicle vehicle) {
        VehicleResponse response = new VehicleResponse();
        response.setId(vehicle.getId());
        response.setName(vehicle.getName());
        response.setRegistrationNo(vehicle.getRegistrationNo());
        response.setType(vehicle.getType());
        response.setCapacity(vehicle.getCapacity());
        response.setDescription(vehicle.getDescription());
        response.setStatus(vehicle.getStatus());
        response.setRejectionReason(vehicle.getRejectionReason());
        response.setCreatedAt(vehicle.getCreatedAt());
        response.setUpdatedAt(vehicle.getUpdatedAt());
//        response.setEditable(isEditable(vehicle));
//        response.setBookable(isBookable(vehicle));
        return response;
    }

    public static VehicleAdminResponse toAdminResponse(Vehicle vehicle) {
        VehicleAdminResponse response = new VehicleAdminResponse();
        response.setId(vehicle.getId());
        response.setName(vehicle.getName());
        response.setRegistrationNo(vehicle.getRegistrationNo());
        response.setType(vehicle.getType());
        response.setCapacity(vehicle.getCapacity());
        response.setDescription(vehicle.getDescription());
        response.setStatus(vehicle.getStatus());
        response.setRejectionReason(vehicle.getRejectionReason());
        response.setCreatedAt(vehicle.getCreatedAt());
        response.setUpdatedAt(vehicle.getUpdatedAt());
//        response.setEditable(isEditable(vehicle));
//        response.setBookable(isBookable(vehicle));

        User owner = vehicle.getOwner();
        response.setOwnerId(owner.getId());
        response.setOwnerName(owner.getName());
        response.setOwnerEmail(owner.getEmail());
        response.setOwnerPhone(owner.getPhone());

        response.setReviewedAt(vehicle.getReviewedAt());
        response.setReviewedByName(vehicle.getApprovedBy() == null ? null : vehicle.getApprovedBy().getName());
        return response;
    }

//    private static boolean isEditable(Vehicle vehicle) {
//        return Boolean.TRUE.equals(vehicle.getActive());
//    }
//
//    private static boolean isBookable(Vehicle vehicle) {
//        return vehicle.getStatus() == VehicleStatus.APPROVED;
//    }
}
