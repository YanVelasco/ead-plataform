package com.ead.authuser.service;

import com.ead.authuser.dtos.UserCourseDto;
import com.ead.authuser.model.UserCourseModel;
import com.ead.authuser.model.UserModel;
import jakarta.validation.Valid;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

public interface UserCourseService {
    @Transactional
    UserCourseModel saveCourseInUser(UserModel user, @Valid UserCourseDto userCourseDto);

    @Transactional
    void deleteCourseInUser(UUID courseId);
}
