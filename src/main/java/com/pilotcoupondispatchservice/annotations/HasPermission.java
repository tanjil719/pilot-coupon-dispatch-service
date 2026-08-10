package com.pilotcoupondispatchservice.annotations;

import com.pilotcoupondispatchservice.constants.PermissionConstant;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = ElementType.METHOD)
public @interface HasPermission {

    public PermissionConstant permission();

}
