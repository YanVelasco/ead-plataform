package com.ead.authuser.validations;

import com.ead.authuser.dtos.UserDto;
import com.ead.authuser.service.UserService;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Slf4j
@Component
public class UserValidator implements Validator {

    private final Validator validator;
    private final UserService userService;

    public UserValidator(@Qualifier("defaultValidator") Validator validator, UserService userService) {
        this.validator = validator;
        this.userService = userService;
    }

    @Override
    public boolean supports(@NonNull Class<?> clazz) {
        return false;
    }

    @Override
    public void validate(@NonNull Object o, @NonNull Errors errors) {
        var userDto = (UserDto) o;
        validator.validate(userDto, errors);
        if (!errors.hasErrors()) {
            existsByUsernameOrEmail(userDto.username(), userDto.email(), errors);
        }
    }

    public void existsByUsernameOrEmail(String username, String email, Errors errors) {
        if (userService.existsByUsernameOrEmail(username, email)) {
            log.warn("Username or email already exists: {} / {}", username, email);
            errors.reject("usernameOrEmail", "Username or email already exists");
        }
    }
}
