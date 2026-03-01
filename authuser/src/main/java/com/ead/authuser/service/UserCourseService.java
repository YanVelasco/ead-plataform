package com.ead.authuser.service;

import com.ead.authuser.dtos.UserCourseDto;
import com.ead.authuser.model.UserCourseModel;
import com.ead.authuser.model.UserModel;
import jakarta.validation.Valid;

public interface UserCourseService {
    UserCourseModel saveCourseInUser(UserModel user, @Valid UserCourseDto userCourseDto);
}
