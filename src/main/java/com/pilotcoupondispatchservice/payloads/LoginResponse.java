package com.pilotcoupondispatchservice.payloads;

import com.pilotcoupondispatchservice.modules.auth.dto.TokenPairResponse;
import com.pilotcoupondispatchservice.modules.users.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class LoginResponse {
    private TokenPairResponse token;
//    private User user;
}
