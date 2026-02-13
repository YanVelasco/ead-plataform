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
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

@Service
public class LessonServiceImpl implements LessonService {

    private final LessonRepository lessonRepository;
    private final ModuleRepository moduleRepository;

    public LessonServiceImpl(LessonRepository lessonRepository, ModuleRepository moduleRepository) {
        this.lessonRepository = lessonRepository;
        this.moduleRepository = moduleRepository;
    }

    @Override
    public void deleteById(UUID lessonId) {
        Optional.of(lessonRepository.deleteLessonById(lessonId))
                .filter(count -> count > 0)
                .orElseThrow(() -> new RuntimeException("Lesson not found with ID: " + lessonId));
    }

    @Override
    public LessonModel save(ModuleModel module, LessonDto lessonDto) {
        var lessonModel = new LessonModel();
        BeanUtils.copyProperties(lessonDto, lessonModel, "lessonId");
        lessonModel.setModule(module);
        lessonModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        return lessonRepository.save(lessonModel);
    }

    @Override
    public Page<LessonModel> getAllLessonsByModule(ModuleModel module, LessonFilter lessonFilter, Pageable pageable) {
        Specification<LessonModel> spec = Specifications.lessonFilters(lessonFilter)
                .and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("module"), module));
        return lessonRepository.findAll(spec, pageable);
    }

    @Override
    public LessonModel findOneLessonInModule(ModuleModel module, UUID lessonId) {
        return lessonRepository.findById(lessonId)
                .filter(lesson -> lesson.getModule().getModuleId().equals(module.getModuleId()))
                .orElseThrow(() -> new NotFoundException("Lesson not found with ID: " + lessonId +  " or lesson is not associated with the module with ID: " + module.getModuleId()));
    }

    @Override
    public LessonModel updateLesson(ModuleModel module, UUID lessonId, LessonDto lessonDto) {
        var lessonModel = findOneLessonInModule(module, lessonId);
        BeanUtils.copyProperties(lessonDto, lessonModel, "lessonId", "creationDate", "module");
        return lessonModel;
    }

}
