package com.ead.course.dtos;

import com.ead.course.enums.CourseLevel;
import com.ead.course.enums.CourseStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CourseDto(
        @NotBlank(message = "Course name is required")
        String name,

        @NotBlank(message = "Course description is required")
        String description,

        @NotNull(message = "Course status is required")
        CourseStatus courseStatus,

        @NotNull(message = "Course level is required")
        CourseLevel courseLevel,

        @NotNull(message = "User instructor is required")
        UUID userInstructor,

        String imageUrl
) {
}
