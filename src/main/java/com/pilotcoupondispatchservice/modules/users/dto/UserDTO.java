package com.pilotcoupondispatchservice.modules.users.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserDTO implements Serializable {

    private Long id;
    private String name;
    private String email;
    private String phone;
    private String password;
//    private RoleLevel role;
    private Boolean isActive;
    private String createdAt;
    private String updatedAt;
}
