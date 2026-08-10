package com.pilotcoupondispatchservice.modules.roles.dto;

import com.pilotcoupondispatchservice.enums.RoleLevel;
import com.pilotcoupondispatchservice.constants.PermissionConstant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RoleDTO implements Serializable {

    private Long id;
    private String alias;
    private RoleLevel roleLevel;
    private List<PermissionConstant> permissionConstantList;
    private Boolean predefine;
    private String createdAt;
    private String updatedAt;
}
