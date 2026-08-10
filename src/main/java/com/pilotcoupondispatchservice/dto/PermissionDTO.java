package com.pilotcoupondispatchservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.pilotcoupondispatchservice.constants.PermissionConstant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by IntelliJ IDEA.
 * User: Md. Shamim
 * Date: ২৫/১১/২২
 * Time: ১০:৫০ PM
 * Email: mdshamim723@gmail.com
 */

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class PermissionDTO {

    private String value;

    private String name;

    // Itch Board
    private String group;

    private String desc;

    private boolean hasPermission;

    public static PermissionDTO getPermissionDTO(PermissionConstant constant) {
        return new PermissionDTO(constant.getValue(), constant.getName(), constant.getGroup(), constant.getDesc(), false);
    }

}
