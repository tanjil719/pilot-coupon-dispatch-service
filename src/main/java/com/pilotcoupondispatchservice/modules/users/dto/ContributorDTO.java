package com.pilotcoupondispatchservice.modules.users.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ContributorDTO implements Serializable {

    private Long id;
    private Long userId;
    private BigDecimal trustScore;
    private Integer totalSessions;
    private Boolean isApproved;
    private Boolean isBanned;
    private String createdAt;
    private String updatedAt;
}
