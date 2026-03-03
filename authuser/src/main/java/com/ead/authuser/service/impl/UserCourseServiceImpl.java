package com.ead.authuser.service.impl;

import com.ead.authuser.dtos.UserCourseDto;
import com.ead.authuser.exceptions.SubscriptionAlreadyExistsException;
import com.ead.authuser.model.UserCourseModel;
import com.ead.authuser.model.UserModel;
import com.ead.authuser.repository.UserCourseRepository;
import com.ead.authuser.service.UserCourseService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserCourseServiceImpl implements UserCourseService {

    private final UserCourseRepository userCourseRepository;

    public UserCourseServiceImpl(UserCourseRepository userCourseRepository) {
        this.userCourseRepository = userCourseRepository;
    }

    @Override
    public UserCourseModel saveCourseInUser(UserModel user, UserCourseDto userCourseDto) {

        if (userCourseRepository.existsByCourseIdAndUser(userCourseDto.courseId(), user)) {
            throw new SubscriptionAlreadyExistsException("User is already subscribed to this course");
        }

        UserCourseModel userCourseModel = new UserCourseModel();
        userCourseModel.setCourseId(userCourseDto.courseId());
        userCourseModel.setUser(user);
        return userCourseRepository.save(userCourseModel);

    }

    @Override
    public void deleteCourseInUser(UUID courseId) {
        userCourseRepository.deleteAllUserCourseModelByCourseId(courseId);
    }

}
