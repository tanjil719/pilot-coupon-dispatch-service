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
public class UserProfileResponse implements Serializable {

    private String name;
    private String email;
    private String phone;
}
