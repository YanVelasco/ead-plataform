package com.ead.course.repositories;

import com.ead.course.models.LessonModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

public interface LessonRepository extends JpaRepository<LessonModel, UUID> {

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("""
        DELETE FROM LessonModel l
        WHERE l.lessonId = :lessonId
    """)
    int deleteLessonById(@Param("lessonId") UUID lessonId);

}
