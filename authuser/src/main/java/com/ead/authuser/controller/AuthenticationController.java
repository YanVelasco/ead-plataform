package com.ead.authuser.controller;

import com.ead.authuser.dtos.UserDto;
import com.ead.authuser.service.UserService;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    final UserService userService;

    public AuthenticationController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<Object> registerUser(
            @RequestBody @JsonView(UserDto.UserView.RegistrationPost.class) @Validated(UserDto.UserView.RegistrationPost.class) UserDto userDto
    ) {
        log.info("POST /auth/signup - registering user with username: {}", userDto.username());
        var createdUser = userService.registerUser(userDto);
        log.info("POST /auth/signup - user registered successfully, id: {}", createdUser.getUserId());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

}
