package com.ead.authuser.dtos;

import com.ead.authuser.enums.UserStatus;
import com.ead.authuser.enums.UserType;

import java.util.UUID;

public record UserFilterDto(
        String username,
        UserType userType,
        UserStatus userStatus,
        String email,
        String fullName,
        UUID courseId
) {
}
