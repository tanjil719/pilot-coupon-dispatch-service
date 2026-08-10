package com.pilotcoupondispatchservice.dao;

import com.pilotcoupondispatchservice.constants.PermissionConstant;
import com.pilotcoupondispatchservice.utils.SecurityUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PermissionServiceImpl implements PermissionService<String> {

    private final int CHAR_BIT_SIZE = 7;


    @Override
    public boolean hasAccessPermission(PermissionConstant permissionConstant) {
        String loggedInUserPermission = SecurityUtil.getLoggedInUser().getPermissionString();
        return isPermissionBitAvailableInPermission(loggedInUserPermission, permissionConstant.ordinal());
    }


    @Override
    public String generatePermission(List<PermissionConstant> permissionConstants) {

        String permissionString = "";
        for (PermissionConstant permissionConstant : permissionConstants) {
            permissionString = setPermissionIndexToString(permissionString, permissionConstant.ordinal());
        }

        return permissionString;
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
