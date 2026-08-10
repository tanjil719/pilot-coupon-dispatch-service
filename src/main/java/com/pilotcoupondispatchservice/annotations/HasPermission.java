package com.pilotcoupondispatchservice.annotations;

import com.pilotcoupondispatchservice.constants.PermissionConstant;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by IntelliJ IDEA.
 * User: Md. Shamim
 * Date: ২৫/১১/২২
 * Time: ১০:৫০ PM
 * Email: mdshamim723@gmail.com
 */

@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = ElementType.METHOD)
public @interface HasPermission {

    public PermissionConstant permission();

}
