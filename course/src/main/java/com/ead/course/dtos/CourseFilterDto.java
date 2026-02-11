package com.ead.course.dtos;

import com.ead.course.enums.CourseLevel;
import com.ead.course.enums.CourseStatus;

import java.util.UUID;

public record CourseFilterDto(
        String name,
        String description,
        CourseStatus courseStatus,
        CourseLevel courseLevel,
        UUID userInstructor
) {
}
