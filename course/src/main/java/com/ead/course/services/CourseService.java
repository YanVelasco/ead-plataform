package com.ead.course.services;

import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

public interface CourseService {

    @Transactional
    void delete(UUID courseId);

}
