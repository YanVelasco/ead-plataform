package com.ead.course.controllers;

import com.ead.course.dtos.SubscriptionDto;
import com.ead.course.services.CourseService;
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

    public CourseUserController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping("/cousers/{courseId}/users")
    public ResponseEntity<Object> getUserByCourseId(
            @PathVariable UUID courseId,
            @PageableDefault(sort = "userId", direction = Sort.Direction.ASC) Pageable pageable
    ) {
        courseService.getById(courseId);
        return ResponseEntity.ok(" "); //TODO: Implementar a lógica para retornar os usuários associados ao curso
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
