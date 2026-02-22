package com.ead.authuser.controller;

import com.ead.authuser.service.UserCourseService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserCourseController {

    private final UserCourseService userCourseService;

    public UserCourseController(UserCourseService userCourseService) {
        this.userCourseService = userCourseService;
    }
}
