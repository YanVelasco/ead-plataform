package com.ead.course.services.impl;

import com.ead.course.services.CourseUserService;
import org.springframework.stereotype.Service;

@Service
public class CourseUserServiceImpl implements CourseUserService {

    private final CourseUserService courseUserService;

    public CourseUserServiceImpl(CourseUserService courseUserService) {
        this.courseUserService = courseUserService;
    }

}
