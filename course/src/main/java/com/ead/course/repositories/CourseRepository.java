package com.ead.course.repositories;

import com.ead.course.models.CourseModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

public interface CourseRepository extends JpaRepository<CourseModel, UUID> {

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("""
                DELETE FROM CourseModel c
                WHERE c.courseId = :courseId
            """)
    int deleteCourseById(@Param("courseId") UUID courseId);

    boolean existsByName(String name);

}
