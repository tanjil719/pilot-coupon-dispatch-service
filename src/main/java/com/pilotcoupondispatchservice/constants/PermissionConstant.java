package com.pilotcoupondispatchservice.constants;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Permission constants for Pilot Coupon Dispatch Service
 */

@AllArgsConstructor
@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum PermissionConstant {

    /* ============ User ===========*/
    READ_USER("View User", "READ_USER", "User-Module", "View all users."),
    CREATE_USER("Create User", "CREATE_USER", "User-Module", "Create user."),
    MODIFY_USER("Modify User", "MODIFY_USER", "User-Module", "Update user."),
    DELETE_USER("Delete User", "DELETE_USER", "User-Module", "Delete user."),
    READ_USER_PROFILE("View User Profile", "READ_USER_PROFILE", "User-Module", "View user profile."),
    MODIFY_USER_PROFILE("Modify User Profile", "MODIFY_USER_PROFILE", "User-Module", "Modify user profile."),
    UPDATE_USER_PASSWORD("Update User Password", "UPDATE_USER_PASSWORD", "User-Module", "Update all user password."),
    UPDATE_USER_PROFILE_PASSWORD("Update User Profile Password", "UPDATE_USER_PROFILE_PASSWORD", "User-Module", "Update profile password."),

    /* ============ Role ===========*/
    READ_ROLE("View Role", "READ_ROLE", "Role-Module", "View all roles."),
    CREATE_ROLE("Create Role", "CREATE_ROLE", "Role-Module", "Create role."),
    MODIFY_ROLE("Modify Role", "MODIFY_ROLE", "Role-Module", "Update role."),
    DELETE_ROLE("Delete Role", "DELETE_ROLE", "Role-Module", "Delete role."),
    READ_PERMISSION("View Permission", "READ_PERMISSION", "Role-Module", "View all permissions."),

    /* ============ Vehicle ===========*/
    VEHICLE_CREATE("Create Vehicle", "VEHICLE_CREATE", "Vehicle-Module", "Create own vehicle."),
    VEHICLE_VIEW_OWN("View Own Vehicle", "VEHICLE_VIEW_OWN", "Vehicle-Module", "View own vehicles."),
    VEHICLE_UPDATE("Update Vehicle", "VEHICLE_UPDATE", "Vehicle-Module", "Update own vehicle."),
    VEHICLE_DELETE("Delete Vehicle", "VEHICLE_DELETE", "Vehicle-Module", "Soft delete own vehicle."),
    VEHICLE_VIEW_ALL("View All Vehicles", "VEHICLE_VIEW_ALL", "Vehicle-Module", "View all vehicles."),
    VEHICLE_REVIEW("Review Vehicle", "VEHICLE_REVIEW", "Vehicle-Module", "Approve or reject a pending vehicle."),

    /* ============ Route ===========*/
    ROUTE_CREATE("Create Route", "ROUTE_CREATE", "Route-Module", "Create a route."),
    ROUTE_UPDATE("Update Route", "ROUTE_UPDATE", "Route-Module", "Update a route."),
    ROUTE_DELETE("Delete Route", "ROUTE_DELETE", "Route-Module", "Soft delete a route."),
    ROUTE_VIEW_ALL("View All Routes", "ROUTE_VIEW_ALL", "Route-Module", "View all routes, including inactive ones."),
    ROUTE_CHANGE_STATUS("Change Route Status", "ROUTE_CHANGE_STATUS", "Route-Module", "Activate or deactivate a route."),
    ROUTE_VIEW("View Route", "ROUTE_VIEW", "Route-Module", "View active routes."),

    /* ============ Coupon ===========*/
    // NOTE: permission bit position = enum ordinal (see PermissionServiceImpl), so new values must
    // always be appended at the end of this enum, never inserted in the middle.
    COUPON_REQUEST_CREATE("Create Coupon Request", "COUPON_REQUEST_CREATE", "Coupon-Module", "Submit a coupon request for an approved vehicle."),
    COUPON_REQUEST_VIEW_OWN("View Own Coupon Requests", "COUPON_REQUEST_VIEW_OWN", "Coupon-Module", "View own coupon requests."),
    COUPON_REQUEST_CANCEL("Cancel Coupon Request", "COUPON_REQUEST_CANCEL", "Coupon-Module", "Cancel own pending coupon request."),
    COUPON_VIEW_OWN("View Own Coupons", "COUPON_VIEW_OWN", "Coupon-Module", "View own coupons."),
    COUPON_REQUEST_VIEW_ALL("View All Coupon Requests", "COUPON_REQUEST_VIEW_ALL", "Coupon-Module", "View all coupon requests."),
    COUPON_ISSUE("Issue Coupon", "COUPON_ISSUE", "Coupon-Module", "Issue a coupon, either against a request or manually."),
    COUPON_REQUEST_REJECT("Reject Coupon Request", "COUPON_REQUEST_REJECT", "Coupon-Module", "Reject a pending coupon request."),
    COUPON_VIEW_ALL("View All Coupons", "COUPON_VIEW_ALL", "Coupon-Module", "View all coupons."),
    COUPON_CANCEL("Cancel Coupon", "COUPON_CANCEL", "Coupon-Module", "Cancel an unused coupon."),
    COUPON_UPDATE("Update Coupon", "COUPON_UPDATE", "Coupon-Module", "Update the amount and expiry of an unused coupon."),

    /* ============ Booking ===========*/
    BOOKING_CREATE("Create Booking", "BOOKING_CREATE", "Booking-Module", "Create a booking against an approved vehicle and an active coupon."),
    BOOKING_VIEW_OWN("View Own Bookings", "BOOKING_VIEW_OWN", "Booking-Module", "View own bookings."),
    BOOKING_CANCEL("Cancel Booking", "BOOKING_CANCEL", "Booking-Module", "Cancel own pending booking."),
    BOOKING_VIEW_ALL("View All Bookings", "BOOKING_VIEW_ALL", "Booking-Module", "View all bookings."),
    BOOKING_STATUS_UPDATE("Update Booking Status", "BOOKING_STATUS_UPDATE", "Booking-Module", "Approve or reject a pending booking."),
    BOOKING_COMPLETE("Complete Booking", "BOOKING_COMPLETE", "Booking-Module", "Start or complete an in-progress booking."),
    BOOKING_ASSIGN_PILOT("Assign Pilot", "BOOKING_ASSIGN_PILOT", "Booking-Module", "Assign a pilot to a booking on approval."),

    /* ============ Pilot ===========*/
    PILOT_CREATE("Create Pilot", "PILOT_CREATE", "Pilot-Module", "Create a pilot."),
    PILOT_UPDATE("Update Pilot", "PILOT_UPDATE", "Pilot-Module", "Update a pilot's details."),
    PILOT_DELETE("Delete Pilot", "PILOT_DELETE", "Pilot-Module", "Soft delete a pilot."),
    PILOT_VIEW_ALL("View All Pilots", "PILOT_VIEW_ALL", "Pilot-Module", "View all pilots and pilot availability."),
    PILOT_CHANGE_STATUS("Change Pilot Status", "PILOT_CHANGE_STATUS", "Pilot-Module", "Change a pilot's employment status."),
    PILOT_SCHEDULE_VIEW("View Pilot Schedules", "PILOT_SCHEDULE_VIEW", "Pilot-Module", "View pilot schedules and calendars."),

    /* ============ Dashboard ===========*/
    // NOTE: permission bit position = enum ordinal (see PermissionServiceImpl), so new values must
    // always be appended at the end of this enum, never inserted in the middle.
    DASHBOARD_VIEW_OWN("View Own Dashboard", "DASHBOARD_VIEW_OWN", "Dashboard-Module", "View the owner landing dashboard."),
    DASHBOARD_VIEW_ALL("View All Dashboard", "DASHBOARD_VIEW_ALL", "Dashboard-Module", "View the admin landing dashboard.");


    private final String value;
    private final String name;
    @JsonIgnore
    private final String group;
    private final String desc;
}
