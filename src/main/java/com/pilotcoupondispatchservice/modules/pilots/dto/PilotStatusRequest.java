package com.pilotcoupondispatchservice.modules.pilots.dto;

import com.pilotcoupondispatchservice.enums.PilotStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PilotStatusRequest {

    @NotNull(message = "Status must not be null")
    private PilotStatus status;
}
