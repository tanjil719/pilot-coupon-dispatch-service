package com.pilotcoupondispatchservice.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by IntelliJ IDEA.
 * User: Md. Shamim
 * Date: ৩/৫/২০
 * Time: ১১:৩৯ AM
 * Email: mdshamim723@gmail.com
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Conditionals {
    Conditional[] value();
}