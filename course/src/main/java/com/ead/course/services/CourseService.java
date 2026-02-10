package com.ead.course.services;

import com.ead.course.dtos.CourseDto;
import com.ead.course.models.CourseModel;
import jakarta.validation.Valid;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

public interface CourseService {

    @Transactional
    void delete(UUID courseId);

    @Transactional
    CourseModel save(@Valid CourseDto courseDto);

    boolean existsByName(String courseName);

}
