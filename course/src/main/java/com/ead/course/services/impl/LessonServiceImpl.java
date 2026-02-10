package com.ead.course.services.impl;

import com.ead.course.repositories.LessonRepository;
import com.ead.course.services.LessonService;
import org.springframework.stereotype.Service;

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

}
