package com.ead.authuser.controller;

import com.ead.authuser.dtos.InstructorDto;
import com.ead.authuser.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/instructors")
public class InstructorController {

    final UserService userService;


    public InstructorController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/subscription")
    public ResponseEntity<Object> subscribeInstructorToCourse(
            @RequestBody @Valid InstructorDto instructorDto
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.subscriptionInstructor(userService.findById(instructorDto.userId())));
    }

}
