package com.ead.course.services;

import com.ead.course.dtos.ModuleDto;
import com.ead.course.dtos.ModuleFilterDto;
import com.ead.course.models.CourseModel;
import com.ead.course.models.ModuleModel;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

public interface ModuleService {

    @Transactional
    void deleteById(UUID moduleId);

    @Transactional
    ModuleModel save(CourseModel course, @Valid ModuleDto moduleDto);

    @Transactional(readOnly = true)
    Page<ModuleModel> getAllModulesByCourse(CourseModel course, ModuleFilterDto filter, Pageable pageable);

    ModuleModel findById(CourseModel course, UUID moduleId);

    @Transactional
    ModuleModel updateModule(CourseModel course, UUID moduleId, @Valid ModuleDto moduleDto);

}
