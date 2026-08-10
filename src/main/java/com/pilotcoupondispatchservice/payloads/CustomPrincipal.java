package com.pilotcoupondispatchservice.payloads;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class CustomPrincipal {

    private long userId;
    private String email;
    private String name;
    private String permissionString;
}
