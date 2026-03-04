package com.ead.course.services;

import com.ead.course.dtos.SubscriptionDto;
import com.ead.course.models.CourseModel;
import com.ead.course.models.CourseUserModel;
import jakarta.validation.Valid;

import java.util.UUID;

public interface CourseUserService {
    CourseUserModel saveSubscriptionUserInCourse(CourseModel course, @Valid SubscriptionDto subscriptionDto);

    void deleteCourseUserByUserId(UUID userId);
}
