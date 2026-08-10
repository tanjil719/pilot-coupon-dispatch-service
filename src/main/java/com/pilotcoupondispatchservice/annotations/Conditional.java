package com.pilotcoupondispatchservice.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by IntelliJ IDEA.
 * User: Md. Shamim
 * Date: ৩/৫/২০
 * Time: ১১:৩৭ AM
 * Email: mdshamim723@gmail.com
 */

@Repeatable(Conditionals.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {ConditionalValidator.class})
public @interface Conditional {

    String message() default "field is required.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String selected();

    String[] values();

    String[] required();

}
