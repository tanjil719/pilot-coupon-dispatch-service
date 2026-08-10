package com.pilotcoupondispatchservice.dao;

import com.pilotcoupondispatchservice.constants.PermissionConstant;
import com.pilotcoupondispatchservice.dto.PermissionDTO;
import com.pilotcoupondispatchservice.enums.RoleLevel;
import com.pilotcoupondispatchservice.modules.roles.entity.Role;
import com.pilotcoupondispatchservice.exceptions.InvalidRequestException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import static com.pilotcoupondispatchservice.utils.SecurityUtil.getLoggedInUserPermission;

/**
 * Created by IntelliJ IDEA.
 * User: Md. Shamim
 * Date: ২৫/১১/২২
 * Time: ১০:৫০ PM
 * Email: mdshamim723@gmail.com
 */

@Service
public class PermissionServiceImpl implements PermissionService {

    private final int CHAR_BIT_SIZE = 7;

    private final I18NService i18NService;

    public PermissionServiceImpl(I18NService i18NService) {
        this.i18NService = i18NService;
    }

    @Override
    public boolean hasAccessPermission(PermissionConstant permissionConstant) {
        String loggedInUserPermission = getLoggedInUserPermission();
        return isPermissionBitAvailableInPermission(loggedInUserPermission, permissionConstant.ordinal());
    }

    @Override
    public Set<PermissionConstant> getAccessiblePermissionList(String permissionString) {

        Set<PermissionConstant> constants = new HashSet<>();

        for (PermissionConstant value : PermissionConstant.values()) {

            if (isPermissionBitAvailableInPermission(permissionString, value.ordinal())) {
                constants.add(value);
            }
        }

        return constants;
    }

    @Override
    public Set<PermissionConstant> getAccessiblePermissionList(List<Role> roles) {

        Set<PermissionConstant> constants = new HashSet<>();

        roles.forEach(role -> {
            for (PermissionConstant value : PermissionConstant.values()) {

                if (isPermissionBitAvailableInPermission(role.getPermission(), value.ordinal())) {
                    constants.add(value);
                }
            }
        });

        return constants;
    }

    @Override
    public Set<String> getNameOfAccessiblePermissionList(List<Role> roles) {

        Set<String> constants = new HashSet<>();

        roles.forEach(role -> {
            for (PermissionConstant value : PermissionConstant.values()) {
                if (isPermissionBitAvailableInPermission(role.getPermission(), value.ordinal())) {
                    constants.add(value.getName());
                }
            }
        });

        return constants;
    }

//    @Override
//    public String generatePermission(List<PermissionConstant> permissionConstants) {
//
//        String permissionString = "";
//
//        for (PermissionConstant permissionConstant : permissionConstants) {
//            permissionString = setPermissionIndexToString(permissionString, permissionConstant.ordinal());
//        }
//
//        return permissionString;
//    }

    @Override
    public String generatePermission(List<PermissionConstant> permissionConstants, RoleLevel roleLevel) {

        if (roleLevel == null) {
            throw new InvalidRequestException(i18NService.getMessage("role.role_level.not_null"));
        }

        String permissionString = "";

        for (PermissionConstant permissionConstant : permissionConstants) {

            /* Todo:
             *   Need to enable this check with proper permissions
             * */

//            if (!permissionConstant.getRoleLevels().contains(roleLevel)) {
//                throw new InvalidRequestException(i18NService.getMessage("role.permission_role_level.not_match"));
//            }

            permissionString = setPermissionIndexToString(permissionString, permissionConstant.ordinal());
        }

        return permissionString;
    }

    @Override
    public Map<String, List<PermissionDTO>> getModuleBasePermissionListFromPermission(String permission, RoleLevel roleLevel) {

        TreeMap<String, List<PermissionDTO>> maps = new TreeMap<>();

        for (PermissionConstant value : PermissionConstant.values()) {

            PermissionDTO permissionDTO = PermissionDTO.getPermissionDTO(value);

            if (!value.getRoleLevels().contains(roleLevel)) {
                continue;
            }

            if (isPermissionBitAvailableInPermission(permission, value.ordinal())) {
                permissionDTO.setHasPermission(true);
            }

            if (!maps.containsKey(permissionDTO.getGroup())) {
                List<PermissionDTO> permissionConstants = new ArrayList<>();
                permissionConstants.add(permissionDTO);
                maps.put(permissionDTO.getGroup(), permissionConstants);
            } else {
                maps.get(permissionDTO.getGroup()).add(permissionDTO);
            }

        }

        return maps;
    }

    @Override
    public Map<String, List<PermissionDTO>> getAllModuleBasePermissionList(RoleLevel roleLevel) {

        TreeMap<String, List<PermissionDTO>> maps = new TreeMap<>();

        for (PermissionConstant value : PermissionConstant.values()) {

            PermissionDTO permissionDTO = PermissionDTO.getPermissionDTO(value);

            if (!value.getRoleLevels().contains(roleLevel)) {
                continue;
            }

            if (!maps.containsKey(permissionDTO.getGroup())) {
                List<PermissionDTO> permissionConstants = new ArrayList<>();
                permissionConstants.add(permissionDTO);
                maps.put(permissionDTO.getGroup(), permissionConstants);
            } else {
                maps.get(permissionDTO.getGroup()).add(permissionDTO);
            }

        }

        return maps;
    }

    private boolean isPermissionBitAvailableInPermission(String permissionString, int maskIndex) {

        int characterIndex = maskIndex / CHAR_BIT_SIZE;
        if ((characterIndex + 1) > permissionString.length()) return false;

        int bit_index = (maskIndex - (characterIndex * CHAR_BIT_SIZE));
        int mask = 1 << bit_index;

        return (permissionString.charAt(characterIndex) & mask) > 0;
    }

    private String setPermissionIndexToString(String permissionString, int maskIndex) {

        int characterIndex = maskIndex / CHAR_BIT_SIZE;

        if ((characterIndex + 1) > permissionString.length()) {
            permissionString = processPermissionString(permissionString, characterIndex + 1);
        }

        char convertedCharacter = setBitTrueInCharacter(permissionString.charAt(characterIndex), maskIndex, characterIndex);
        StringBuilder builder = new StringBuilder(permissionString);
        builder.setCharAt(characterIndex, convertedCharacter);

        return builder.toString();
    }

    private char setBitTrueInCharacter(char charAt, int maskIndex, int characterIndex) {
        int mask = 1 << (maskIndex - (characterIndex * CHAR_BIT_SIZE));
        return asciiIndexToChar((charAt | mask));
    }

    private char setBitFalseInCharacter(char charAt, int maskIndex, int characterIndex) {
        int mask = 1 << (maskIndex - (characterIndex * CHAR_BIT_SIZE));
        mask ^= 255;
        return asciiIndexToChar((charAt & mask));
    }

    private String processPermissionString(String permissionString, int characterIndex) {
        return String.format("%s%s", permissionString, StringUtils.repeat(asciiIndexToChar(0), characterIndex - permissionString.length()));
    }

    private char asciiIndexToChar(int index) {
        return (char) index;
    }


}
