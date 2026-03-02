package com.ead.authuser.dtos;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record InstructorDto(

        @NotNull(message = "UserId cannot be null")
        UUID userId

) {
}
