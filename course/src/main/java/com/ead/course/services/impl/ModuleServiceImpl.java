package com.ead.course.services.impl;

import com.ead.course.dtos.ModuleDto;
import com.ead.course.models.CourseModel;
import com.ead.course.models.ModuleModel;
import com.ead.course.repositories.ModuleRepository;
import com.ead.course.services.ModuleService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

@Service
public class ModuleServiceImpl implements ModuleService {

    private final ModuleRepository moduleRepository;

    public ModuleServiceImpl(ModuleRepository moduleRepository) {
        this.moduleRepository = moduleRepository;
    }

    @Override
    public void deleteById(UUID moduleId) {
        Optional.of(moduleRepository.deleteModuleById(moduleId))
                .filter(count -> count > 0)
                .orElseThrow(() -> new RuntimeException("Module not found with ID: " + moduleId));
    }

    @Override
    @Transactional
    public ModuleModel save(CourseModel course, ModuleDto moduleDto) {
        var moduleModel = new ModuleModel();
        BeanUtils.copyProperties(moduleDto, moduleModel, "moduleId");
        moduleModel.setCourse(course);
        moduleModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        return moduleRepository.save(moduleModel);
    }

}
