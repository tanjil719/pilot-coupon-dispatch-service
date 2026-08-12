package com.pilotcoupondispatchservice.constants;

public class Constant {

    /* ===== Role ===== */
    public static final String ADMIN_ROLE_ALIAS = "ADMIN";
    public static final String OWNER_ROLE_ALIAS = "OWNER";


    /* ======== User Type Base Permission ========*/
    public static final PermissionConstant[] ADMIN_PERMISSION_LIST = {
            PermissionConstant.READ_USER,
            PermissionConstant.CREATE_USER,
            PermissionConstant.MODIFY_USER,
            PermissionConstant.DELETE_USER,
            PermissionConstant.READ_USER_PROFILE,
            PermissionConstant.READ_ROLE,
            PermissionConstant.CREATE_ROLE,
            PermissionConstant.MODIFY_ROLE,
            PermissionConstant.DELETE_ROLE,
            PermissionConstant.VEHICLE_VIEW_ALL,
            PermissionConstant.VEHICLE_REVIEW,
            PermissionConstant.ROUTE_CREATE,
            PermissionConstant.ROUTE_UPDATE,
            PermissionConstant.ROUTE_VIEW_ALL,
            PermissionConstant.ROUTE_CHANGE_STATUS,
            PermissionConstant.COUPON_REQUEST_VIEW_ALL,
            PermissionConstant.COUPON_ISSUE,
            PermissionConstant.COUPON_REQUEST_REJECT,
            PermissionConstant.COUPON_VIEW_ALL,
            PermissionConstant.COUPON_CANCEL,
            PermissionConstant.COUPON_UPDATE,
            PermissionConstant.BOOKING_VIEW_ALL,
            PermissionConstant.BOOKING_STATUS_UPDATE,
            PermissionConstant.BOOKING_COMPLETE,
            PermissionConstant.BOOKING_ASSIGN_PILOT,
            PermissionConstant.PILOT_CREATE,
            PermissionConstant.PILOT_UPDATE,
            PermissionConstant.PILOT_DELETE,
            PermissionConstant.PILOT_VIEW_ALL,
            PermissionConstant.PILOT_CHANGE_STATUS,
            PermissionConstant.PILOT_SCHEDULE_VIEW,
            PermissionConstant.DASHBOARD_VIEW_ALL,
    };

    public static final PermissionConstant[] OWNER_PERMISSION_LIST = {
            PermissionConstant.READ_USER_PROFILE,
            PermissionConstant.READ_ROLE,
            PermissionConstant.VEHICLE_CREATE,
            PermissionConstant.VEHICLE_VIEW_OWN,
            PermissionConstant.VEHICLE_UPDATE,
            PermissionConstant.VEHICLE_DELETE,
            PermissionConstant.ROUTE_VIEW,
            PermissionConstant.COUPON_REQUEST_CREATE,
            PermissionConstant.COUPON_REQUEST_VIEW_OWN,
            PermissionConstant.COUPON_REQUEST_CANCEL,
            PermissionConstant.COUPON_VIEW_OWN,
            PermissionConstant.BOOKING_CREATE,
            PermissionConstant.BOOKING_VIEW_OWN,
            PermissionConstant.BOOKING_CANCEL,
            PermissionConstant.DASHBOARD_VIEW_OWN,
    };

}
