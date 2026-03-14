package com.ead.course.validations;

import com.ead.course.dtos.CourseDto;
import com.ead.course.services.CourseService;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.UUID;

@Slf4j
@Component
public class CourseValidator implements Validator {

    private final Validator validator;
    private final CourseService courseService;


    public CourseValidator(@Qualifier("defaultValidator") Validator validator, CourseService courseService) {
        this.validator = validator;
        this.courseService = courseService;
    }

    @Override
    public boolean supports(@NonNull Class<?> clazz) {
        return false;
    }

    @Override
    public void validate(@NonNull Object o, @NonNull Errors errors) {
        log.info("Validating object: {}", o);
        var courseDto = (CourseDto) o;
        validator.validate(courseDto, errors);

        if (!errors.hasErrors()) {
            validateCourseName(courseDto, errors);
            validateUserInstructor(courseDto.userInstructor(), errors);
            log.info("Validation successful for courseDto: {}", courseDto);
        } else {
            log.info("Validation failed for courseDto: {} - Errors: {}", courseDto, errors.getAllErrors());
        }

    }

    private void validateCourseName(CourseDto courseDto, Errors errors) {
        if (courseService.existsByName(courseDto.name())) {
            log.info("Course name already exists - name: {}", courseDto.name());
            errors.rejectValue("name", "courseNameConflict", "Course name already exists");
        }
    }

    private void validateUserInstructor(UUID instructorId, Errors errors) {
//        UserDto userInstructor = authUserClient.getUserById(instructorId);
//        log.info("User DTO Response: {}", userInstructor);
//        log.info("User instructor DTO details - userId: {}, username: {}, email: {}, fullName: {}, userStatus: {}, userType: {}, phoneNumber: {}",
//                userInstructor.userId(),
//                userInstructor.username(),
//                userInstructor.email(),
//                userInstructor.fullName(),
//                userInstructor.userStatus(),
//                userInstructor.userType(),
//                userInstructor.phoneNumber());
//
//        if (userInstructor.userType().equals(UserType.USER) || userInstructor.userType().equals(UserType.STUDENT)){
//            log.info("User instructor must be of type INSTRUCTOR - userId: {}, userType: {}", instructorId, userInstructor.userType());
//            errors.rejectValue("userInstructor", "invalidUserInstructor", "User instructor must be of type INSTRUCTOR");
//        }
    }

}
