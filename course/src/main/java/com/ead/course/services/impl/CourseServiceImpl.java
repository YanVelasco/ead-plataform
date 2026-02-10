package com.ead.course.services.impl;

import com.ead.course.repositories.CourseRepository;
import com.ead.course.services.CourseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;

    public CourseServiceImpl(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Override
    @Transactional
    public void delete(UUID courseId) {
        Optional.of(courseRepository.deleteCourseById(courseId))
                .filter(count -> count > 0)
                .orElseThrow(() -> new RuntimeException("Course not found with ID: " + courseId));
    }

}
