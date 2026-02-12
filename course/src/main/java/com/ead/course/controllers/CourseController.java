package com.ead.course.controllers;

import com.ead.course.dtos.CourseDto;
import com.ead.course.dtos.CourseFilterDto;
import com.ead.course.dtos.PageDto;
import com.ead.course.models.CourseModel;
import com.ead.course.services.CourseService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/courses")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @PostMapping
    public ResponseEntity<Object> saveCourse(
            @RequestBody @Valid CourseDto courseDto
    ) {
        if (courseService.existsByName(courseDto.name())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Course with name '" + courseDto.name() + "' " +
                    "already exists.");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(courseService.save(courseDto));
    }

    @GetMapping
    public ResponseEntity<PageDto<CourseModel>> getAllCourses(
            @ModelAttribute CourseFilterDto filter,
            Pageable pageable
    ) {
        return ResponseEntity.ok().body(PageDto.from(courseService.getAllCourses(filter, pageable)));
    }

    @GetMapping("/{courseId}")
    public ResponseEntity<Object> getCourseById(@PathVariable UUID courseId) {
        return ResponseEntity.status(HttpStatus.OK).body(courseService.getById(courseId));
    }

    @DeleteMapping("{courseId}")
    public ResponseEntity<Object> deleteCourse(@PathVariable UUID courseId) {
        courseService.delete(courseId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

}
