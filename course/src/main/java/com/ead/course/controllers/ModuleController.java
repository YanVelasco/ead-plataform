package com.ead.course.controllers;

import com.ead.course.dtos.ModuleDto;
import com.ead.course.dtos.ModuleFilterDto;
import com.ead.course.dtos.PageDto;
import com.ead.course.models.ModuleModel;
import com.ead.course.services.CourseService;
import com.ead.course.services.ModuleService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
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
        log.info("POST /courses/{}/modules - title: {}", courseId, moduleDto.title());
        var course = courseService.getById(courseId);
        var savedModule = moduleService.save(course, moduleDto);
        log.info("Module created - courseId: {}, moduleId: {}", courseId, savedModule.getModuleId());
        return ResponseEntity.status(HttpStatus.CREATED).body(savedModule);
    }

    @GetMapping("/courses/{courseId}/modules")
    public ResponseEntity<PageDto<ModuleModel>> getAllModulesByCourseId(@PathVariable UUID courseId
    , @ModelAttribute ModuleFilterDto filter, Pageable pageable){
        log.info("GET /courses/{}/modules - filter: {}, pageable: {}", courseId, filter, pageable);
        var course = courseService.getById(courseId);
        return ResponseEntity.ok().body(PageDto.from(moduleService.getAllModulesByCourse(course, filter, pageable)));
    }

    @DeleteMapping("/courses/{courseId}/modules/{moduleId}")
    public ResponseEntity<Object> deleteModule(@PathVariable UUID courseId, @PathVariable UUID moduleId){
        log.info("DELETE /courses/{}/modules/{}", courseId, moduleId);
        courseService.getById(courseId);
        getModuleById(courseId, moduleId);
        moduleService.deleteById(moduleId);
        log.info("Module deleted - courseId: {}, moduleId: {}", courseId, moduleId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/courses/{courseId}/modules/{moduleId}")
    public ResponseEntity<Object> getModuleById(@PathVariable UUID courseId, @PathVariable UUID moduleId){
        log.info("GET /courses/{}/modules/{}", courseId, moduleId);
        var course = courseService.getById(courseId);
        return ResponseEntity.status(HttpStatus.OK).body(moduleService.findById(course, moduleId));
    }

    @PutMapping("/courses/{courseId}/modules/{moduleId}")
    public ResponseEntity<Object> updateModule(@PathVariable UUID courseId, @PathVariable UUID moduleId, @RequestBody @Valid ModuleDto moduleDto) {
        log.info("PUT /courses/{}/modules/{} - title: {}", courseId, moduleId, moduleDto.title());
        var course = courseService.getById(courseId);
        return ResponseEntity.ok().body(moduleService.updateModule(course, moduleId, moduleDto));
    }
}
