package com.ead.course.repositories;

import com.ead.course.models.ModuleModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

public interface ModuleRepository extends JpaRepository<ModuleModel, UUID> {

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("""
        DELETE FROM ModuleModel m
        WHERE m.moduleId = :moduleId
    """)
    int deleteModuleById(@Param("moduleId") UUID moduleId);

}
