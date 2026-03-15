package com.ead.course.services.impl;

import com.ead.course.dtos.PageDto;
import com.ead.course.dtos.UserFilterDto;
import com.ead.course.models.UserModel;
import com.ead.course.repositories.UserRepository;
import com.ead.course.services.UserService;
import com.ead.course.specifications.Specifications;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public PageDto<UserModel> getAllUsersByCourse(UUID courseId, UserFilterDto filter, Pageable pageable) {
        var users = userRepository.findAll(Specifications.userFilters(filter, courseId), pageable);
        return PageDto.from(users);
    }
}
