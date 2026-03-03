package com.ead.authuser.controller;

import com.ead.authuser.clients.CourseClient;
import com.ead.authuser.dtos.CourseDto;
import com.ead.authuser.dtos.PageDto;
import com.ead.authuser.dtos.UserCourseDto;
import com.ead.authuser.service.UserCourseService;
import com.ead.authuser.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
public class UserCourseController {

    final CourseClient courseClient;
    final UserCourseService userCourseService;
    final UserService userService;

    public UserCourseController(CourseClient courseClient, UserCourseService userCourseService, UserService userService) {
        this.courseClient = courseClient;
        this.userCourseService = userCourseService;
        this.userService = userService;
    }

    @GetMapping("/users/{userId}/courses")
    public ResponseEntity<PageDto<CourseDto>> getAllCoursesByUser(
            @PathVariable UUID userId,
            @PageableDefault(sort = "courseId", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(courseClient.getAllCoursesByUser(userId, pageable));
    }

    @PostMapping("/users/{userId}/courses/subscription")
    public ResponseEntity<Object> saveCourseInUser(
            @PathVariable UUID userId,
            @RequestBody @Valid UserCourseDto userCourseDto
    ) {
        var user = userService.findById(userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(userCourseService.saveCourseInUser(user, userCourseDto));
    }

    @DeleteMapping("/users/courses/{courseId}")
    public ResponseEntity<Object> deleteCourseInUser(
            @PathVariable UUID courseId
    ) {
        log.info("deleteCourseInUser - courseId: {}", courseId);
        userCourseService.deleteCourseInUser(courseId);
        return ResponseEntity.status(HttpStatus.OK).body("Course deleted in user successfully");
    }

}
