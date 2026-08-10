package com.pilotcoupondispatchservice.dao;

import com.pilotcoupondispatchservice.constants.PermissionConstant;

import java.util.List;

public interface PermissionService<T> {

    public boolean hasAccessPermission(PermissionConstant permissionConstant);

//    public Set<PermissionConstant> getAccessiblePermissionList(String permission);
//
//    public Set<PermissionConstant> getAccessiblePermissionList(List<Role> roles);
//
//    public Set<String> getNameOfAccessiblePermissionList(List<Role> roles);

    public String generatePermission(List<PermissionConstant> permissionConstants);

}
