package com.ead.authuser.service;

import com.ead.authuser.dtos.UserCourseDto;
import com.ead.authuser.model.UserCourseModel;
import com.ead.authuser.model.UserModel;
import jakarta.validation.Valid;

import java.util.UUID;

public interface UserCourseService {
    UserCourseModel saveCourseInUser(UserModel user, @Valid UserCourseDto userCourseDto);

    void deleteCourseInUser(UUID courseId);
}
