package com.ead.authuser.dtos;

public record UserDto(
        String username,
        String email,
        String password,
        String oldPassword,
        String fullName,
        String phoneNumber,
        String imageUrl
) {
}
