package com.ead.course.controllers;

import com.ead.course.dtos.ModuleDto;
import com.ead.course.dtos.ModuleFilterDto;
import com.ead.course.dtos.PageDto;
import com.ead.course.models.ModuleModel;
import com.ead.course.services.CourseService;
import com.ead.course.services.ModuleService;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.data.domain.Pageable;
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

    @GetMapping("/courses/{courseId}/modules")
    public ResponseEntity<PageDto<ModuleModel>> getAllModulesByCourseId(@PathVariable UUID courseId
    , @ModelAttribute ModuleFilterDto filter, Pageable pageable){
        var course = courseService.getById(courseId);
        return ResponseEntity.ok().body(PageDto.from(moduleService.getAllModulesByCourse(course, filter, pageable)));
    }

    @DeleteMapping("/courses/{courseId}/modules/{moduleId}")
    public ResponseEntity<Object> deleteModule(@PathVariable UUID courseId, @PathVariable UUID moduleId){
        courseService.getById(courseId);
        getModuleById(courseId, moduleId);
        moduleService.deleteById(moduleId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/courses/{courseId}/modules/{moduleId}")
    public ResponseEntity<Object> getModuleById(@PathVariable UUID courseId, @PathVariable UUID moduleId){
        var course = courseService.getById(courseId);
        return ResponseEntity.status(HttpStatus.OK).body(moduleService.findById(course, moduleId));
    }

    @PutMapping("/courses/{courseId}/modules/{moduleId}")
    public ResponseEntity<Object> updateModule(@PathVariable UUID courseId, @PathVariable UUID moduleId, @RequestBody @Valid ModuleDto moduleDto) {
        var course = courseService.getById(courseId);
        return ResponseEntity.ok().body(moduleService.updateModule(course, moduleId, moduleDto));
    }
}
