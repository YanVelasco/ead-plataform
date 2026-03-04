package com.ead.course.services.impl;

import com.ead.course.clients.AuthUserClient;
import com.ead.course.dtos.SubscriptionDto;
import com.ead.course.enums.UserStatus;
import com.ead.course.exceptions.SubscriptionAlreadyExistsException;
import com.ead.course.exceptions.UserIsBlockedException;
import com.ead.course.models.CourseModel;
import com.ead.course.models.CourseUserModel;
import com.ead.course.repositories.CourseUserRepository;
import com.ead.course.services.CourseUserService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class CourseUserServiceImpl implements CourseUserService {

    private final CourseUserRepository courseUserRepository;
    private final AuthUserClient authUserClient;

    public CourseUserServiceImpl(CourseUserRepository courseUserRepository, AuthUserClient authUserClient) {
        this.courseUserRepository = courseUserRepository;
        this.authUserClient = authUserClient;
    }

    @Override
    @Transactional
    @CacheEvict(value = "courses-users-page", allEntries = true)
    public CourseUserModel saveSubscriptionUserInCourse(CourseModel course, SubscriptionDto subscriptionDto) {

        var userDto = authUserClient.getUserById(subscriptionDto.userId());

        if (userDto.userStatus().equals(UserStatus.BLOCKED)) {
            throw new UserIsBlockedException("User is blocked and cannot subscribe to courses.");
        }

        if (!courseUserRepository.existsByCourseAndUserId(course, subscriptionDto.userId())) {
            CourseUserModel courseUserModel = new CourseUserModel();
            courseUserModel.setCourse(course);
            courseUserModel.setUserId(subscriptionDto.userId());
            var courseUser = courseUserRepository.save(courseUserModel);

            authUserClient.saveUserSubscriptionInAuthUser(courseUserModel.getUserId(), courseUserModel.getCourse().getCourseId());

            return courseUser;

        } else {
            throw new SubscriptionAlreadyExistsException("User already subscribed to this course.");
        }

    }

    @Override
    public void deleteCourseUserByUserId(UUID userId) {
        var courseUsers = courseUserRepository.findAllByUserId(userId);
        if (!courseUsers.isEmpty()) {
            courseUserRepository.deleteAll(courseUsers);
        }
    }
}
