package com.ead.course.services;

import com.ead.course.dtos.ModuleDto;
import com.ead.course.models.CourseModel;
import com.ead.course.models.ModuleModel;
import jakarta.validation.Valid;

import java.util.UUID;

public interface ModuleService {

    void deleteById(UUID moduleId);

    ModuleModel save(CourseModel course, @Valid ModuleDto moduleDto);

}
