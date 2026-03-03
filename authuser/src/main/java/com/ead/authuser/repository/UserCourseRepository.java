package com.ead.authuser.repository;

import com.ead.authuser.model.UserCourseModel;
import com.ead.authuser.model.UserModel;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface UserCourseRepository extends JpaRepository<UserCourseModel, UUID>,
        JpaSpecificationExecutor<UserCourseModel> {
    boolean existsByCourseIdAndUser(@NotNull(message = "CourseId cannot be null") UUID uuid, UserModel user);

    @Query("SELECT uc FROM UserCourseModel uc WHERE uc.user.userId = :userId")
    List<UserCourseModel> findAllByUser(UUID userId);

}
