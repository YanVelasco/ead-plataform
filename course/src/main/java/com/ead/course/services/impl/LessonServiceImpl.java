package com.ead.course.services.impl;

import com.ead.course.dtos.LessonDto;
import com.ead.course.models.LessonModel;
import com.ead.course.models.ModuleModel;
import com.ead.course.repositories.LessonRepository;
import com.ead.course.services.LessonService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

@Service
public class LessonServiceImpl implements LessonService {

    private final LessonRepository lessonRepository;

    public LessonServiceImpl(LessonRepository lessonRepository) {
        this.lessonRepository = lessonRepository;
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

}
