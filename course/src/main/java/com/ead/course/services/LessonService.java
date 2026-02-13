package com.ead.course.services;

import com.ead.course.dtos.LessonDto;
import com.ead.course.dtos.LessonFilter;
import com.ead.course.models.LessonModel;
import com.ead.course.models.ModuleModel;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

public interface LessonService {

    @Transactional
    void deleteById(UUID lessonId);

    @Transactional
    LessonModel save(ModuleModel module, @Valid LessonDto lessonDto);

    Page<LessonModel> getAllLessonsByModule(ModuleModel module, LessonFilter lessonFilter, Pageable pageable);

    LessonModel findOneLessonInModule(ModuleModel module, UUID lessonId);

    @Transactional
    LessonModel updateLesson(ModuleModel module, UUID lessonId, @Valid LessonDto lessonDto);

}
