package com.ead.authuser.dtos;

import com.ead.authuser.enums.CourseLevel;
import com.ead.authuser.enums.CourseStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public record CourseDto(
        UUID courseId,
        UUID userInstructor,
        String name,
        String description,
        String imageUrl,
        CourseLevel courseLevel,
        CourseStatus courseStatus
) {
}
