package com.pilotcoupondispatchservice.constants;

public class Constant {

    /* ===== Role ===== */
    public static final String ADMIN_ROLE_ALIAS = "ADMIN";
    public static final String OWNER_ROLE_ALIAS = "OWNER";
//    public static final String AFFILIATOR_ROLE_ALIAS = "Affiliator";


    /* ======== User Type Base Permission ========*/
    public static final PermissionConstant[] ADMIN_PERMISSION_LIST = {
            PermissionConstant.READ_USER,
            PermissionConstant.CREATE_USER,
            PermissionConstant.MODIFY_USER,
            PermissionConstant.DELETE_USER,
            PermissionConstant.READ_ROLE,
//            PermissionConstant.CREATE_ROLE,
//            PermissionConstant.MODIFY_ROLE,
//            PermissionConstant.DELETE_ROLE,
    };

}
