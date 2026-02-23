package com.ead.course.controllers;

import com.ead.course.services.CourseUserService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CourseUserController {

    private final CourseUserService courseUserService;

    public CourseUserController(CourseUserService courseUserService) {
        this.courseUserService = courseUserService;
    }
}
