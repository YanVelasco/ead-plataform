package com.ead.course.repositories;

import com.ead.course.models.CourseModel;
import com.ead.course.models.CourseUserModel;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CourseUserRepository extends JpaRepository<CourseUserModel, UUID> {
    boolean existsByCourseAndUserId(CourseModel course, @NotNull(message = "userId cannot be null") UUID uuid);
}
