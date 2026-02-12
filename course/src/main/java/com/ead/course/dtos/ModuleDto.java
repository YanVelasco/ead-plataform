package com.ead.course.dtos;

import jakarta.validation.constraints.NotBlank;

public record ModuleDto(

        @NotBlank(message = "Module name is required")
        String title,

        @NotBlank(message = "Module description is required")
        String description

) {
}
