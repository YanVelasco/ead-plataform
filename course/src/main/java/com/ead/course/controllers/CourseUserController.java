package com.ead.course.controllers;

import com.ead.course.dtos.PageDto;
import com.ead.course.dtos.SubscriptionDto;
import com.ead.course.dtos.UserFilterDto;
import com.ead.course.models.UserModel;
import com.ead.course.services.CourseService;
import com.ead.course.services.UserService;
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

    private final CourseService courseService;
    private final UserService userService;

    public CourseUserController(CourseService courseService, UserService userService) {
        this.courseService = courseService;
        this.userService = userService;
    }

    @GetMapping("/cousers/{courseId}/users")
    public ResponseEntity<PageDto<UserModel>> getUserByCourseId(
            @PathVariable UUID courseId,
            @ModelAttribute UserFilterDto filter,
            @PageableDefault(sort = "userId", direction = Sort.Direction.ASC) Pageable pageable
    ) {
        courseService.getById(courseId);
        return ResponseEntity.status(HttpStatus.OK).body(userService.getAllUsersByCourse(courseId, filter, pageable));
    }

    @PostMapping("/courses/{courseId}/users/subscription")
    public ResponseEntity<Object> saveSubscriptionUserInCourse(
            @PathVariable UUID courseId,
            @RequestBody @Valid SubscriptionDto subscriptionDto
    ) {
        var course = courseService.getById(courseId);
        return ResponseEntity.status(HttpStatus.OK).body(" "); //TODO: Implementar a lógica para salvar a inscrição do usuário no curso
    }

}
