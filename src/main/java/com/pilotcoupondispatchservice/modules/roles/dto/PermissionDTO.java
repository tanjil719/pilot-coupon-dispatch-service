package com.pilotcoupondispatchservice.modules.roles.dto;

import com.pilotcoupondispatchservice.constants.PermissionConstant;
import com.pilotcoupondispatchservice.enums.RoleLevel;
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
public class PermissionDTO implements Serializable {

    private PermissionConstant permission;
    private String value;
    private String name;
    private String group;
    private String desc;
    private List<RoleLevel> roleLevels;
    private boolean selected;
}
