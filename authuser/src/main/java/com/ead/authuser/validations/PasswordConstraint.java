package com.ead.authuser.validations;

import jakarta.validation.Constraint;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PasswordConstraintImpl.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface PasswordConstraint {

    String message() default
            """ 
                Invalid password! Password must contain at least one digit [0-9],
                one lowercase character [a-z], one uppercase character [A-Z],
                one special character [!@#$%^&*()-+=],
                no whitespace, and be at least 6 and maximum of 20 characters.
            """;

    Class<?>[] groups() default {};
    Class<?>[] payload() default {};

}
