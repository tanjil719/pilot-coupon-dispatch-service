//package com.pilotcoupondispatchservice.modules.roles.service;
//
//import com.pilotcoupondispatchservice.constants.PermissionConstant;
//import com.pilotcoupondispatchservice.enums.RoleLevel;
//import com.pilotcoupondispatchservice.modules.roles.dto.PermissionDTO;
//import org.springframework.stereotype.Service;
//
//import java.util.*;
//import java.util.stream.Collectors;
//
//@Service
//public class PermissionService {
//
//    private final int CHAR_BIT_SIZE = 7;
//
//    private static final String PERMISSION_DELIMITER = ",";
//
//    /**
//     * Generate permission string from a list of PermissionConstants
//     */
//    public String generatePermission(List<PermissionConstant> permissionList) {
//        if (permissionList == null || permissionList.isEmpty()) {
//            return "";
//        }
//
//        String permissionString = "";
//        for (PermissionConstant permissionConstant : permissionList) {
//            permissionString = setPermissionIndexToString(permissionString, permissionConstant.);
//        }
//
//        return permissionString;
//    }
//
//    private String setPermissionIndexToString(String permissionString, int maskIndex) {
//
//        int characterIndex = maskIndex / CHAR_BIT_SIZE;
//
//        if ((characterIndex + 1) > permissionString.length()) {
//            permissionString = processPermissionString(permissionString, characterIndex + 1);
//        }
//
//        char convertedCharacter = setBitTrueInCharacter(permissionString.charAt(characterIndex), maskIndex, characterIndex);
//        StringBuilder builder = new StringBuilder(permissionString);
//        builder.setCharAt(characterIndex, convertedCharacter);
//
//        return builder.toString();
//    }
//
//    /**
//     * Parse permission string to list of PermissionConstants
//     */
//    public Set<PermissionConstant> parsePermission(String permissionString) {
//        if (permissionString == null || permissionString.isEmpty()) {
//            return new HashSet<>();
//        }
//
//        return Arrays.stream(permissionString.split(PERMISSION_DELIMITER))
//                .map(String::trim)
//                .map(this::getPermissionByName)
//                .filter(Optional::isPresent)
//                .map(Optional::get)
//                .collect(Collectors.toSet());
//    }
//
//    /**
//     * Get permission by name
//     */
//    private Optional<PermissionConstant> getPermissionByName(String name) {
//        try {
//            return Optional.of(PermissionConstant.valueOf(name));
//        } catch (IllegalArgumentException e) {
//            return Optional.empty();
//        }
//    }
//
//    /**
//     * Check if a user has specific permission
//     */
//    public boolean hasPermission(String userPermissionString, PermissionConstant requiredPermission) {
//        Set<PermissionConstant> permissions = parsePermission(userPermissionString);
//        return permissions.contains(requiredPermission);
//    }
//
//    /**
//     * Get all permissions grouped by module for a specific role level
//     */
//    public Map<String, List<PermissionDTO>> getAllModuleBasePermissionList(RoleLevel roleLevel) {
//        Map<String, List<PermissionDTO>> groupedPermissions = new HashMap<>();
//
//        for (PermissionConstant perm : PermissionConstant.values()) {
//            if (perm.getRoleLevels().contains(roleLevel)) {
//                String group = perm.getGroup();
//
//                PermissionDTO permDTO = new PermissionDTO();
//                permDTO.setPermission(perm);
//                permDTO.setValue(perm.getValue());
//                permDTO.setName(perm.getName());
//                permDTO.setDesc(perm.getDesc());
//                permDTO.setRoleLevels(perm.getRoleLevels());
//
//                groupedPermissions.computeIfAbsent(group, k -> new ArrayList<>())
//                        .add(permDTO);
//            }
//        }
//
//        return groupedPermissions;
//    }
//
//    /**
//     * Get module-based permission list from permission string
//     */
//    public Map<String, List<PermissionDTO>> getModuleBasePermissionListFromPermission(String permissionString, RoleLevel roleLevel) {
//        Set<PermissionConstant> userPermissions = parsePermission(permissionString);
//        Map<String, List<PermissionDTO>> groupedPermissions = new HashMap<>();
//
//        for (PermissionConstant perm : PermissionConstant.values()) {
//            if (perm.getRoleLevels().contains(roleLevel)) {
//                String group = perm.getGroup();
//
//                PermissionDTO permDTO = new PermissionDTO();
//                permDTO.setPermission(perm);
//                permDTO.setValue(perm.getValue());
//                permDTO.setName(perm.getName());
//                permDTO.setDesc(perm.getDesc());
//                permDTO.setRoleLevels(perm.getRoleLevels());
//                permDTO.setSelected(userPermissions.contains(perm));
//
//                groupedPermissions.computeIfAbsent(group, k -> new ArrayList<>())
//                        .add(permDTO);
//            }
//        }
//
//        return groupedPermissions;
//    }
//
//    /**
//     * Get accessible permission list for a role
//     */
//    public Set<PermissionConstant> getAccessiblePermissionList(String permissionString) {
//        return parsePermission(permissionString);
//    }
//}
