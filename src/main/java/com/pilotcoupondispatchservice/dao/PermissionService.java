package com.pilotcoupondispatchservice.dao;

import com.pilotcoupondispatchservice.constants.PermissionConstant;
import com.pilotcoupondispatchservice.dto.PermissionDTO;
import com.pilotcoupondispatchservice.enums.RoleLevel;
import com.pilotcoupondispatchservice.modules.roles.entity.Role;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface PermissionService<T> {

    public boolean hasAccessPermission(PermissionConstant permissionConstant);

//    public Set<PermissionConstant> getAccessiblePermissionList(String permission);
//
//    public Set<PermissionConstant> getAccessiblePermissionList(List<Role> roles);
//
//    public Set<String> getNameOfAccessiblePermissionList(List<Role> roles);

    public String generatePermission(List<PermissionConstant> permissionConstants);

}
