package com.ead.course.services;

import com.ead.course.dtos.CourseDto;
import com.ead.course.dtos.CourseFilterDto;
import com.ead.course.models.CourseModel;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

public interface CourseService {

    @Transactional
    String delete(UUID courseId);

    @Transactional
    CourseModel save(@Valid CourseDto courseDto);

    boolean existsByName(String courseName);

    @Transactional(readOnly = true)
    Page<CourseModel> getAllCourses(CourseFilterDto filter, Pageable pageable);

    CourseModel getById(UUID courseId);

}
