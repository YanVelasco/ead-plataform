package com.ead.course.controllers;

import com.ead.course.clients.AuthUserClient;
import com.ead.course.dtos.PageDto;
import com.ead.course.dtos.SubscriptionDto;
import com.ead.course.dtos.UserDto;
import com.ead.course.services.CourseService;
import com.ead.course.services.CourseUserService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class CourseUserController {

    private final CourseUserService courseUserService;
    private final AuthUserClient authUserClient;
    private final CourseService courseService;

    public CourseUserController(CourseUserService courseUserService, AuthUserClient authUserClient, CourseService courseService) {
        this.courseUserService = courseUserService;
        this.authUserClient = authUserClient;
        this.courseService = courseService;
    }

    @GetMapping("/cousers/{courseId}/users")
    public ResponseEntity<PageDto<UserDto>> getUserByCourseId(
            @PathVariable UUID courseId,
            @PageableDefault(sort = "userId", direction = Sort.Direction.ASC) Pageable pageable
    ) {
        courseService.getById(courseId);
        return ResponseEntity.ok(authUserClient.getAllUsersByCourse(courseId, pageable));
    }

    @PostMapping("/courses/{courseId}/users/subscription")
    public ResponseEntity<Object> saveSubscriptionUserInCourse(
            @PathVariable UUID courseId,
            @RequestBody @Valid SubscriptionDto subscriptionDto
    ) {
        var course = courseService.getById(courseId);
        return ResponseEntity.status(HttpStatus.OK).body(courseUserService.saveSubscriptionUserInCourse(course, subscriptionDto));
    }

}
