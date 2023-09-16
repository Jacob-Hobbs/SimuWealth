package com.projects.simuwealth.simuwealth.CustomAnnotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = EmailExistsValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface EmailExists {

    String message() default "User with this email does not exist.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
