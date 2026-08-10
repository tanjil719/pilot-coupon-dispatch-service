package com.pilotcoupondispatchservice.modules.roles.service;

import com.pilotcoupondispatchservice.annotations.HasPermission;
import com.pilotcoupondispatchservice.constants.PermissionConstant;
import com.pilotcoupondispatchservice.dao.PermissionService;
import com.pilotcoupondispatchservice.modules.roles.dto.RoleDTO;
import com.pilotcoupondispatchservice.modules.roles.entity.Role;
import com.pilotcoupondispatchservice.modules.roles.repository.RoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;
    private final PermissionService permissionService;

    @HasPermission(permission = PermissionConstant.READ_ROLE)
    public Page<Role> searchRole(String alias, Pageable pageable) {
        return roleRepository.findAllByAliasContainingIgnoreCase(alias, pageable);
    }

//    @HasPermission(permission = PermissionConstant.READ_ROLE)
//    public Map<String, Object> findRoleById(Long id) {
//        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
//
//        return roleRepository.findById(id)
//                .map(role -> new HashMap<String, Object>() {{
//                    put("id", role.getId());
//                    put("alias", role.getAlias());
//                    put("predefine", role.isPredefine());
//                    put("roleLevel", role.getRoleLevel());
//                    put("createdAt", role.getCreatedAt() == null ? null : dateTimeFormatter.format(role.getCreatedAt()));
//                    put("updatedAt", role.getUpdatedAt() == null ? null : dateTimeFormatter.format(role.getUpdatedAt()));
//                    put("createdBy", role.getCreatedBy());
//                    put("updatedBy", role.getUpdatedBy());
//                    put("permissions", permissionService.getModuleBasePermissionListFromPermission(role.getPermission(), role.getRoleLevel()));
//                }})
//                .orElseThrow(() -> new RuntimeException("Role not found with id: " + id));
//    }

    @HasPermission(permission = PermissionConstant.CREATE_ROLE)
    public Role createRole(RoleDTO roleDTO) {
        if (roleRepository.existsByAlias(roleDTO.getAlias())) {
            throw new RuntimeException("Role with alias already exists: " + roleDTO.getAlias());
        }

        Role role = new Role();
        role.setAlias(roleDTO.getAlias());
        role.setPredefine(false);

        String permission = permissionService.generatePermission(roleDTO.getPermissionConstantList());
        role.setPermission(permission);

        return roleRepository.save(role);
    }

//    @HasPermission(permission = PermissionConstant.MODIFY_ROLE)
//    public Role updateRole(RoleDTO roleDTO, Long id) {
//        return roleRepository.findById(id).map(oldRole -> {
//            if (!oldRole.isPredefine()) {
//                if (!oldRole.getAlias().equals(roleDTO.getAlias()) &&
//                    roleRepository.existsByAlias(roleDTO.getAlias())) {
//                    throw new RuntimeException("Role with alias already exists: " + roleDTO.getAlias());
//                }
//                oldRole.setAlias(roleDTO.getAlias());
//                oldRole.setRoleLevel(roleDTO.getRoleLevel());
//            }
//
//            String permission = permissionService.generatePermission(roleDTO.getPermissionConstantList(), roleDTO.getRoleLevel());
//            oldRole.setPermission(permission);
//
//            return roleRepository.save(oldRole);
//        }).orElseThrow(() -> new RuntimeException("Role not found with id: " + id));
//    }

    @HasPermission(permission = PermissionConstant.DELETE_ROLE)
    public void deleteRole(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found with id: " + id));

        if (role.isPredefine()) {
            throw new RuntimeException("Cannot delete predefined role");
        }

        roleRepository.deleteById(id);
    }

//    @HasPermission(permission = PermissionConstant.READ_PERMISSION)
//    public Map<String, List<PermissionDTO>> findAllModuleBasePermission(RoleLevel roleLevel) {
//        return permissionService.getAllModuleBasePermissionList(roleLevel);
//    }

    public Role findRoleByIdInternal(Long id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found with id: " + id));
    }

    public Optional<Role> findByAlias(String alias) {
        return roleRepository.findByAlias(alias);
    }
}
