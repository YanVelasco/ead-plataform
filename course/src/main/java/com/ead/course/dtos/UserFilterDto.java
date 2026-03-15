package com.ead.course.dtos;

public record UserFilterDto(
        String fullName,
        String email,
        String userStatus,
        String userType
) {
}
