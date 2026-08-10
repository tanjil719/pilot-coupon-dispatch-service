package com.pilotcoupondispatchservice.constants;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pilotcoupondispatchservice.enums.RoleLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

import static com.pilotcoupondispatchservice.enums.RoleLevel.*;

/**
 * Permission constants for Pilot Coupon Dispatch Service
 * Role Levels: ADMIN, CONTRIBUTOR, CONSUMER
 */

@AllArgsConstructor
@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum PermissionConstant {

    /* ============ User ===========*/
    READ_USER("View User", "READ_USER", "User-Module", List.of(ADMIN), "View all users."),
    CREATE_USER("Create User", "CREATE_USER", "User-Module", List.of(ADMIN), "Create user."),
    MODIFY_USER("Modify User", "MODIFY_USER", "User-Module", List.of(ADMIN), "Update user."),
    DELETE_USER("Delete User", "DELETE_USER", "User-Module", List.of(ADMIN), "Delete user."),
    READ_USER_PROFILE("View User Profile", "READ_USER_PROFILE", "User-Module", List.of(ADMIN, CONTRIBUTOR, CONSUMER), "View user profile."),
    MODIFY_USER_PROFILE("Modify User Profile", "MODIFY_USER_PROFILE", "User-Module", List.of(ADMIN, CONTRIBUTOR, CONSUMER), "Modify user profile."),
    UPDATE_USER_PASSWORD("Update User Password", "UPDATE_USER_PASSWORD", "User-Module", List.of(ADMIN), "Update all user password."),
    UPDATE_USER_PROFILE_PASSWORD("Update User Profile Password", "UPDATE_USER_PROFILE_PASSWORD", "User-Module", List.of(ADMIN, CONTRIBUTOR, CONSUMER), "Update profile password."),

    /* ============ Role ===========*/
    READ_ROLE("View Role", "READ_ROLE", "Role-Module", List.of(ADMIN), "View all roles."),
    CREATE_ROLE("Create Role", "CREATE_ROLE", "Role-Module", List.of(ADMIN), "Create role."),
    MODIFY_ROLE("Modify Role", "MODIFY_ROLE", "Role-Module", List.of(ADMIN), "Update role."),
    DELETE_ROLE("Delete Role", "DELETE_ROLE", "Role-Module", List.of(ADMIN), "Delete role."),
    READ_PERMISSION("View Permission", "READ_PERMISSION", "Role-Module", List.of(ADMIN), "View all permissions."),

    /* ============ Contributor ===========*/
    READ_CONTRIBUTOR("View Contributor", "READ_CONTRIBUTOR", "Contributor-Module", List.of(ADMIN), "View all contributors."),
    CREATE_CONTRIBUTOR("Create Contributor", "CREATE_CONTRIBUTOR", "Contributor-Module", List.of(ADMIN), "Create contributor."),
    MODIFY_CONTRIBUTOR("Modify Contributor", "MODIFY_CONTRIBUTOR", "Contributor-Module", List.of(ADMIN), "Update contributor."),
    DELETE_CONTRIBUTOR("Delete Contributor", "DELETE_CONTRIBUTOR", "Contributor-Module", List.of(ADMIN), "Delete contributor."),
    APPROVE_CONTRIBUTOR("Approve Contributor", "APPROVE_CONTRIBUTOR", "Contributor-Module", List.of(ADMIN), "Approve contributor."),
    BAN_CONTRIBUTOR("Ban Contributor", "BAN_CONTRIBUTOR", "Contributor-Module", List.of(ADMIN), "Ban contributor."),

    /* ============ Train ===========*/
    READ_TRAIN("View Train", "READ_TRAIN", "Train-Module", List.of(ADMIN, CONTRIBUTOR, CONSUMER), "View train information."),
    CREATE_TRAIN("Create Train", "CREATE_TRAIN", "Train-Module", List.of(ADMIN), "Create train."),
    MODIFY_TRAIN("Modify Train", "MODIFY_TRAIN", "Train-Module", List.of(ADMIN), "Update train."),
    DELETE_TRAIN("Delete Train", "DELETE_TRAIN", "Train-Module", List.of(ADMIN), "Delete train."),

    /* ============ Train Live Tracking ===========*/
    READ_TRAIN_LIVE_STATUS("View Train Live Status", "READ_TRAIN_LIVE_STATUS", "Train-Tracking-Module", List.of(ADMIN, CONTRIBUTOR, CONSUMER), "View live train status."),
    CREATE_TRAIN_SESSION("Create Tracking Session", "CREATE_TRAIN_SESSION", "Train-Tracking-Module", List.of(CONTRIBUTOR), "Create tracking session."),
    SUBMIT_TRAIN_GPS_LOG("Submit GPS Log", "SUBMIT_TRAIN_GPS_LOG", "Train-Tracking-Module", List.of(CONTRIBUTOR), "Submit GPS tracking data."),

    /* ============ Incidents & Reports ===========*/
    READ_INCIDENT("View Incident", "READ_INCIDENT", "Incident-Module", List.of(ADMIN, CONSUMER), "View incidents."),
    CREATE_INCIDENT("Create Incident", "CREATE_INCIDENT", "Incident-Module", List.of(ADMIN), "Create incident."),
    MODIFY_INCIDENT("Modify Incident", "MODIFY_INCIDENT", "Incident-Module", List.of(ADMIN), "Update incident."),
    READ_REPORT("View Report", "READ_REPORT", "Report-Module", List.of(ADMIN), "View reports."),
    CREATE_REPORT("Create Report", "CREATE_REPORT", "Report-Module", List.of(CONSUMER), "Submit report."),
    MODIFY_REPORT("Modify Report", "MODIFY_REPORT", "Report-Module", List.of(ADMIN), "Update report."),

    /* ============ Favorites ===========*/
    READ_FAVORITE("View Favorite", "READ_FAVORITE", "Favorite-Module", List.of(CONSUMER), "View favorites."),
    CREATE_FAVORITE("Add Favorite", "CREATE_FAVORITE", "Favorite-Module", List.of(CONSUMER), "Add to favorites."),
    DELETE_FAVORITE("Remove Favorite", "DELETE_FAVORITE", "Favorite-Module", List.of(CONSUMER), "Remove from favorites.");

    private final String value;
    private final String name;
    @JsonIgnore
    private final String group;
    private final List<RoleLevel> roleLevels;
    private final String desc;
}
