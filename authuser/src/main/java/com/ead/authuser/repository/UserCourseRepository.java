package com.ead.authuser.repository;

import com.ead.authuser.model.UserCourseModel;
import com.ead.authuser.model.UserModel;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface UserCourseRepository extends JpaRepository<UserCourseModel, UUID>,
        JpaSpecificationExecutor<UserCourseModel> {
    boolean existsByCourseIdAndUser(@NotNull(message = "CourseId cannot be null") UUID uuid, UserModel user);

    void deleteAllUserCourseModelByUser_UserId(UUID userId);
}
