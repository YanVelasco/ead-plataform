package com.ead.course.controllers;

import com.ead.course.clients.AuthUserClient;
import com.ead.course.dtos.PageDto;
import com.ead.course.dtos.UserDto;
import com.ead.course.services.CourseUserService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class CourseUserController {

    private final CourseUserService courseUserService;
    private final AuthUserClient authUserClient;

    public CourseUserController(CourseUserService courseUserService, AuthUserClient authUserClient) {
        this.courseUserService = courseUserService;
        this.authUserClient = authUserClient;
    }

    @GetMapping("/cousers/{courseId}/users")
    public ResponseEntity<PageDto<UserDto>> getUserByCourseId(
            @PathVariable UUID courseId,
            @PageableDefault(sort = "userId", direction = Sort.Direction.ASC) Pageable pageable
    ) {
        return ResponseEntity.ok(authUserClient.getAllUsersByCourse(courseId, pageable));
    }

}
