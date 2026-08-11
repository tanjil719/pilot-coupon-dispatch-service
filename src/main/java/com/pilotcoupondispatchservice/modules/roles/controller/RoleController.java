package com.pilotcoupondispatchservice.modules.roles.controller;

import com.pilotcoupondispatchservice.modules.roles.dto.RoleDTO;
import com.pilotcoupondispatchservice.modules.roles.entity.Role;
import com.pilotcoupondispatchservice.modules.roles.service.RoleService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${url.base}/secured/roles")
@AllArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @GetMapping("/search")
    public ResponseEntity<?> searchRoles(
            @RequestParam(defaultValue = "") String alias,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Role> roles = roleService.searchRole(alias, pageable);
        return ResponseEntity.ok(roles);
    }

//    @GetMapping("/{id}")
//    @HasPermission(permission = PermissionConstant.READ_ROLE)
//    public ResponseEntity<?> getRoleById(@PathVariable Long id) {
//        Map<String, Object> role = roleService.findRoleById(id);
//        return ResponseEntity.ok(role);
//    }

    @PostMapping
    public ResponseEntity<?> createRole(@RequestBody RoleDTO roleDTO) {
        Role role = roleService.createRole(roleDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(role);
    }

//    @PutMapping("/{id}")
//    @HasPermission(permission = PermissionConstant.MODIFY_ROLE)
//    public ResponseEntity<?> updateRole(
//            @PathVariable Long id,
//            @RequestBody RoleDTO roleDTO) {
//        Role role = roleService.updateRole(roleDTO, id);
//        return ResponseEntity.ok(role);
//    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRole(@PathVariable Long id) {
        roleService.deleteRole(id);
        return ResponseEntity.noContent().build();
    }

//    @GetMapping("/permissions/{roleLevel}")
//    @HasPermission(permission = PermissionConstant.READ_PERMISSION)
//    public ResponseEntity<?> getPermissions(
//            @PathVariable RoleLevel roleLevel) {
//        Map<String, List<PermissionDTO>> permissions = roleService.findAllModuleBasePermission(roleLevel);
//        return ResponseEntity.ok(permissions);
//    }
}
