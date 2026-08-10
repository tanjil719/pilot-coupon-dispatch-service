package com.pilotcoupondispatchservice.dao;

import com.pilotcoupondispatchservice.constants.PermissionConstant;
import com.pilotcoupondispatchservice.dto.PermissionDTO;
import com.pilotcoupondispatchservice.enums.RoleLevel;
import com.pilotcoupondispatchservice.modules.roles.entity.Role;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Md. Shamim
 * Date: ২৫/১১/২২
 * Time: ১০:৫০ PM
 * Email: mdshamim723@gmail.com
 */

public interface PermissionService {

    public boolean hasAccessPermission(PermissionConstant permissionConstant);

    public Set<PermissionConstant> getAccessiblePermissionList(String permission);

    public Set<PermissionConstant> getAccessiblePermissionList(List<Role> roles);

    public Set<String> getNameOfAccessiblePermissionList(List<Role> roles);

//    public String generatePermission(List<PermissionConstant> permissionConstants);

    public String generatePermission(List<PermissionConstant> permissionConstants, RoleLevel roleLevel);

    public Map<String, List<PermissionDTO>> getModuleBasePermissionListFromPermission(String permission, RoleLevel roleLevel);

    public Map<String, List<PermissionDTO>> getAllModuleBasePermissionList(RoleLevel roleLevel);

}
