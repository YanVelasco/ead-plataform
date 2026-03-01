package com.ead.authuser.dtos;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record UserCourseDto(
        @NotNull(message = "CourseId cannot be null")
        UUID courseId
) {
}
