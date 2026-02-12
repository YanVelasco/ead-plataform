package com.ead.course.controllers;

import com.ead.course.dtos.ModuleDto;
import com.ead.course.services.CourseService;
import com.ead.course.services.ModuleService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping
public class ModuleController {

    private final ModuleService moduleService;
    private final CourseService courseService;

    public ModuleController(ModuleService moduleService, CourseService courseService) {
        this.moduleService = moduleService;
        this.courseService = courseService;
    }

    @PostMapping("/courses/{courseId}/modules")
    public ResponseEntity<Object> saveModule(
            @PathVariable UUID courseId,
            @RequestBody @Valid ModuleDto moduleDto
    ){
        var course = courseService.getById(courseId);
        return ResponseEntity.status(HttpStatus.CREATED).body(moduleService.save(course, moduleDto));
    }


}
