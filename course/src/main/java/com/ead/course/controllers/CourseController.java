package com.ead.course.controllers;

import com.ead.course.dtos.CourseDto;
import com.ead.course.dtos.CourseFilterDto;
import com.ead.course.dtos.PageDto;
import com.ead.course.models.CourseModel;
import com.ead.course.services.CourseService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
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
        log.info("POST /courses - name: {}", courseDto.name());
        if (courseService.existsByName(courseDto.name())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Course with name '" + courseDto.name() + "' " +
                    "already exists.");
        }
        var savedCourse = courseService.save(courseDto);
        log.info("Course created - courseId: {}", savedCourse.getCourseId());
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCourse);
    }

    @GetMapping
    public ResponseEntity<PageDto<CourseModel>> getAllCourses(
            @ModelAttribute CourseFilterDto filter,
            Pageable pageable
    ) {
        log.info("GET /courses - filter: {}, pageable: {}", filter, pageable);
        return ResponseEntity.ok().body(PageDto.from(courseService.getAllCourses(filter, pageable)));
    }

    @GetMapping("/{courseId}")
    public ResponseEntity<Object> getCourseById(@PathVariable UUID courseId) {
        log.info("GET /courses/{}", courseId);
        return ResponseEntity.status(HttpStatus.OK).body(courseService.getById(courseId));
    }

    @DeleteMapping("{courseId}")
    public ResponseEntity<Object> deleteCourse(@PathVariable UUID courseId) {
        log.info("DELETE /courses/{}", courseId);
        courseService.delete(courseId);
        log.info("Course deleted - courseId: {}", courseId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/{courseId}")
    public ResponseEntity<Object> updateCourse(@PathVariable UUID courseId, @RequestBody @Valid CourseDto courseDto) {
        log.info("PUT /courses/{} - name: {}", courseId, courseDto.name());
        return ResponseEntity.status(HttpStatus.OK).body(courseService.updateById(courseId, courseDto));
    }

}
