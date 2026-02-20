package com.ead.course.services.impl;

import com.ead.course.dtos.LessonDto;
import com.ead.course.dtos.LessonFilter;
import com.ead.course.exceptions.NotFoundException;
import com.ead.course.models.LessonModel;
import com.ead.course.models.ModuleModel;
import com.ead.course.repositories.LessonRepository;
import com.ead.course.repositories.ModuleRepository;
import com.ead.course.services.LessonService;
import com.ead.course.specifications.Specifications;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

@Slf4j
@Service
public class LessonServiceImpl implements LessonService {

    private final LessonRepository lessonRepository;
    private final ModuleRepository moduleRepository;

    public LessonServiceImpl(LessonRepository lessonRepository, ModuleRepository moduleRepository) {
        this.lessonRepository = lessonRepository;
        this.moduleRepository = moduleRepository;
    }

    @Override
    public void deleteById(UUID moduleId, UUID lessonId) {
        log.info("Deleting lesson - moduleId: {}, lessonId: {}", moduleId, lessonId);
        int deletedCount = lessonRepository.deleteLessonByIdAndModuleId(lessonId, moduleId);
        if (deletedCount <= 0) {
            log.warn("Lesson not found for delete - moduleId: {}, lessonId: {}", moduleId, lessonId);
            throw new NotFoundException("Lesson not found with ID: " + lessonId +
                    " or lesson is not associated with the module with ID: " + moduleId);
        }
        log.info("Lesson deleted successfully - moduleId: {}, lessonId: {}", moduleId, lessonId);
    }

    @Override
    public LessonModel save(ModuleModel module, LessonDto lessonDto) {
        log.info("Saving lesson - moduleId: {}, title: {}", module.getModuleId(), lessonDto.title());
        var lessonModel = new LessonModel();
        BeanUtils.copyProperties(lessonDto, lessonModel, "lessonId");
        lessonModel.setModule(module);
        lessonModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        var savedLesson = lessonRepository.save(lessonModel);
        log.info("Lesson saved successfully - lessonId: {}, moduleId: {}", savedLesson.getLessonId(),
                module.getModuleId());
        return savedLesson;
    }

    @Override
    public Page<LessonModel> getAllLessonsByModule(ModuleModel module, LessonFilter lessonFilter, Pageable pageable) {
        log.info("Finding lessons - moduleId: {}, filter: {}, pageable: {}", module.getModuleId(), lessonFilter,
                pageable);
        Specification<LessonModel> spec = Specifications.lessonFilters(lessonFilter)
                .and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("module"), module));
        return lessonRepository.findAll(spec, pageable);
    }

    @Override
    public LessonModel findOneLessonInModule(ModuleModel module, UUID lessonId) {
        log.info("Finding lesson - moduleId: {}, lessonId: {}", module.getModuleId(), lessonId);
        return lessonRepository.findById(lessonId)
                .filter(lesson -> lesson.getModule().getModuleId().equals(module.getModuleId()))
                .orElseThrow(() -> new NotFoundException("Lesson not found with ID: " + lessonId +
                        " or lesson is not associated with the module with ID: " + module.getModuleId()));
    }

    @Override
    public LessonModel updateLesson(ModuleModel module, UUID lessonId, LessonDto lessonDto) {
        log.info("Updating lesson - moduleId: {}, lessonId: {}, title: {}", module.getModuleId(), lessonId,
                lessonDto.title());
        var lessonModel = findOneLessonInModule(module, lessonId);
        BeanUtils.copyProperties(lessonDto, lessonModel, "lessonId", "creationDate", "module");
        log.info("Lesson updated successfully - moduleId: {}, lessonId: {}", module.getModuleId(), lessonId);
        return lessonModel;
    }

}
