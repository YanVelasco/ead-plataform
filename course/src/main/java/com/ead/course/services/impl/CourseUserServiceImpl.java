package com.ead.course.services.impl;

import com.ead.course.clients.AuthUserClient;
import com.ead.course.dtos.SubscriptionDto;
import com.ead.course.exceptions.SubscriptionAlreadyExistsException;
import com.ead.course.models.CourseModel;
import com.ead.course.models.CourseUserModel;
import com.ead.course.repositories.CourseUserRepository;
import com.ead.course.services.CourseUserService;
import org.springframework.stereotype.Service;

@Service
public class CourseUserServiceImpl implements CourseUserService {

    private final CourseUserRepository courseUserRepository;
    private final AuthUserClient authUserClient;

    public CourseUserServiceImpl(CourseUserRepository courseUserRepository, AuthUserClient authUserClient) {
        this.courseUserRepository = courseUserRepository;
        this.authUserClient = authUserClient;
    }

    @Override
    public CourseUserModel saveSubscriptionUserInCourse(CourseModel course, SubscriptionDto subscriptionDto) {
        if (!courseUserRepository.existsByCourseAndUserId(course, subscriptionDto.userId())) {
            authUserClient.getUserById(subscriptionDto.userId());
            CourseUserModel courseUserModel = new CourseUserModel();
            courseUserModel.setCourse(course);
            courseUserModel.setUserId(subscriptionDto.userId());
            return courseUserRepository.save(courseUserModel);
        } else {
            throw new SubscriptionAlreadyExistsException("User already subscribed to this course.");
        }
    }
}
