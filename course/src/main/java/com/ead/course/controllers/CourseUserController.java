package com.ead.course.controllers;

import org.springframework.web.bind.annotation.RestController;

@RestController
public class CourseUserController {

    private final CourseUserController courseUserController;

    public CourseUserController(CourseUserController courseUserController) {
        this.courseUserController = courseUserController;
    }
}
