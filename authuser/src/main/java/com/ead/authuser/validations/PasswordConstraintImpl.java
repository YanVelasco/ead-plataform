package com.ead.authuser.validations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

// Password must contain at least one digit [0-9],
// one lowercase character [a-z], one uppercase character [A-Z],
// one special character [!@#$%^&*()-+=],
// no whitespace, and be at least 6 and maximum of 20 characters.
public class PasswordConstraintImpl implements ConstraintValidator<PasswordConstraint, String> {

    private static final String PASSWORD_PATTERN =
            "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*()\\-+=])(?=\\S+$).{6,20}$";

    private static final Pattern PATTERN = Pattern.compile(PASSWORD_PATTERN);

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        // Se password for null, considere válido aqui; combine com @NotNull se quiser obrigatoriedade
        if (password == null) {
            return true;
        }
        return PATTERN.matcher(password).matches();
    }

}
