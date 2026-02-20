package com.ead.course.controllers;

import com.ead.course.dtos.LessonDto;
import com.ead.course.dtos.LessonFilter;
import com.ead.course.dtos.PageDto;
import com.ead.course.models.LessonModel;
import com.ead.course.services.LessonService;
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
public class LessonController {

    private final LessonService lessonService;
    private final ModuleService moduleService;

    public LessonController(LessonService lessonService, ModuleService moduleService) {
        this.lessonService = lessonService;
        this.moduleService = moduleService;
    }

    @PostMapping("/modules/{moduleId}/lessons")
    public ResponseEntity<Object> saveLesson(
            @PathVariable UUID moduleId,
            @RequestBody @Valid LessonDto lessonDto
    ){
        log.info("POST /modules/{}/lessons - title: {}", moduleId, lessonDto.title());
        var module = moduleService.findById(moduleId);
        var savedLesson = lessonService.save(module, lessonDto);
        log.info("Lesson created - moduleId: {}, lessonId: {}", moduleId, savedLesson.getLessonId());
        return ResponseEntity.ok(savedLesson);
    }

    @GetMapping("/modules/{moduleId}/lessons")
    public ResponseEntity<PageDto<LessonModel>> getAllLessonsByModuleId(@PathVariable UUID moduleId, @ModelAttribute LessonFilter lessonFilter, Pageable pageable){
        log.info("GET /modules/{}/lessons - filter: {}, pageable: {}", moduleId, lessonFilter, pageable);
        var module = moduleService.findById(moduleId);
        return ResponseEntity.status(HttpStatus.OK).body(PageDto.from(lessonService.getAllLessonsByModule(module, lessonFilter, pageable)));
    }

    @GetMapping("/modules/{moduleId}/lessons/{lessonId}")
    public ResponseEntity<Object> getLessonById(@PathVariable UUID moduleId, @PathVariable UUID lessonId){
        log.info("GET /modules/{}/lessons/{}", moduleId, lessonId);
        var module = moduleService.findById(moduleId);
        return ResponseEntity.status(HttpStatus.OK).body(lessonService.findOneLessonInModule(module, lessonId));
    }

    @DeleteMapping("/modules/{moduleId}/lessons/{lessonId}")
    public ResponseEntity<Object> deleteLesson(@PathVariable UUID moduleId, @PathVariable UUID lessonId){
        log.info("DELETE /modules/{}/lessons/{}", moduleId, lessonId);
        lessonService.deleteById(moduleId, lessonId);
        log.info("Lesson deleted - moduleId: {}, lessonId: {}", moduleId, lessonId);
        return ResponseEntity.noContent().build();
    }


    @PutMapping("/modules/{moduleId}/lessons/{lessonId}")
    public ResponseEntity<Object> updateLesson(@PathVariable UUID moduleId, @PathVariable UUID lessonId, @RequestBody @Valid LessonDto lessonDto) {
        log.info("PUT /modules/{}/lessons/{} - title: {}", moduleId, lessonId, lessonDto.title());
        var module = moduleService.findById(moduleId);
        return ResponseEntity.status(HttpStatus.OK).body(lessonService.updateLesson(module, lessonId, lessonDto));

    }

}
