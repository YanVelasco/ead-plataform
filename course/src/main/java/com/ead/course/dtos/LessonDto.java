package com.ead.course.dtos;

import jakarta.validation.constraints.NotBlank;

public record LessonDto(
        @NotBlank(message = "Lesson title is required")
        String title,

        @NotBlank(message = "Lesson description is required")
        String description,

        @NotBlank(message = "Lesson video url is required")
        String videoUrl
) {
}
