package com.ead.course.services;

import com.ead.course.dtos.PageDto;
import com.ead.course.dtos.UserFilterDto;
import com.ead.course.models.UserModel;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface UserService {
    PageDto<UserModel> getAllUsersByCourse(UUID courseId, UserFilterDto filter, Pageable pageable);
}
