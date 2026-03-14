package com.ead.authuser.controller;

import com.ead.authuser.clients.CourseClient;
import com.ead.authuser.dtos.CourseDto;
import com.ead.authuser.dtos.PageDto;
import com.ead.authuser.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Slf4j
@RestController
public class UserCourseController {

    final CourseClient courseClient;
    final UserService userService;

    public UserCourseController(CourseClient courseClient, UserService userService) {
        this.courseClient = courseClient;
        this.userService = userService;
    }

    @GetMapping("/users/{userId}/courses")
    public ResponseEntity<PageDto<CourseDto>> getAllCoursesByUser(
            @PathVariable UUID userId,
            @PageableDefault(sort = "courseId", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(courseClient.getAllCoursesByUser(userId, pageable));
    }

}
