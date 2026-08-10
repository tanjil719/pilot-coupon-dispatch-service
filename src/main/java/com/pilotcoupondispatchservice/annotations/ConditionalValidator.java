package com.pilotcoupondispatchservice.annotations;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanWrapperImpl;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Arrays;

/**
 * Created by IntelliJ IDEA.
 * User: Md. Shamim
 * Date: ৩/৫/২০
 * Time: ১১:৪০ AM
 * Email: mdshamim723@gmail.com
 */

@Slf4j
public class ConditionalValidator implements ConstraintValidator<Conditional, Object> {

    private String selected;
    private String[] required;
    private String message;
    private String[] values;

    @Override
    public void initialize(Conditional requiredIfChecked) {
        selected = requiredIfChecked.selected();
        required = requiredIfChecked.required();
        message = requiredIfChecked.message();
        values = requiredIfChecked.values();
    }

    @Override
    public boolean isValid(Object objectToValidate, ConstraintValidatorContext context) {

        boolean valid = true;

        try {
            BeanWrapperImpl beanWrapper = new BeanWrapperImpl(objectToValidate);
            Object givenValueForSelectedField = beanWrapper.getPropertyValue(selected);
            if (Arrays.asList(values).contains(givenValueForSelectedField)) {
                for (String propertyName : required) {
                    Object givenValue = beanWrapper.getPropertyValue(propertyName);
                    valid = givenValue != null && !"".contentEquals(givenValue.toString());
                    if (!valid) {
                        context.disableDefaultConstraintViolation();
                        context.buildConstraintViolationWithTemplate(
                            String.format(
                                "%s%s %s",
                                String.valueOf(propertyName.charAt(0)).toUpperCase(),
                                propertyName.length() > 1 ? propertyName.substring(1) : "",
                                message
                            )
                        ).addPropertyNode(propertyName).addConstraintViolation();
                    }
                }
            }
        } catch (Exception e) {
            log.error("An exception occurred while accessing class : {}, exception : ", objectToValidate.getClass().getName(), e);
            return false;
        }

        return valid;
    }
}
