package com.pilotcoupondispatchservice.modules.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ActionItemDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private String type;
    private String message;
    private Long relatedId;
}
