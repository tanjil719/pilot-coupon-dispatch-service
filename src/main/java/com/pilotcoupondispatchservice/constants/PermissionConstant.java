package com.pilotcoupondispatchservice.constants;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.pilotcoupondispatchservice.enums.RoleLevel.OWNER;

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

    MODIFY_USER_PROFILE("Modify User Profile","MODIFY_USER_PROFILE","User-Module", "Modify user profile."),

    UPDATE_USER_PASSWORD("Update User Password","UPDATE_USER_PASSWORD","User-Module","Update all user password."),

    UPDATE_USER_PROFILE_PASSWORD("Update User Profile Password","UPDATE_USER_PROFILE_PASSWORD","User-Module", "Update profile password."),

    /* ============ Role ===========*/
    READ_ROLE("View Role","READ_ROLE","Role-Module","View all roles."),

    CREATE_ROLE("Create Role","CREATE_ROLE","Role-Module","Create role."),

    MODIFY_ROLE("Modify Role","MODIFY_ROLE","Role-Module","Update role."),

    DELETE_ROLE("Delete Role","DELETE_ROLE","Role-Module","Delete role."),

    READ_PERMISSION("View Permission","READ_PERMISSION","Role-Module","View all permissions.");


    private final String value;
    private final String name;
    @JsonIgnore
    private final String group;
    private final String desc;
}
