package com.ead.course.services.impl;

import com.ead.course.dtos.ModuleDto;
import com.ead.course.dtos.ModuleFilterDto;
import com.ead.course.models.CourseModel;
import com.ead.course.models.ModuleModel;
import com.ead.course.repositories.ModuleRepository;
import com.ead.course.services.ModuleService;
import com.ead.course.specifications.Specifications;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "modules", key = "{#filter.title, #filter.description, #pageable.pageNumber, #pageable.pageSize, #pageable.sort}")
    public Page<ModuleModel> getAllModulesByCourse(CourseModel course, ModuleFilterDto filter, Pageable pageable) {
        Specification<ModuleModel> spec = Specifications.moduleFilters(filter);
        return moduleRepository.findAll(spec, pageable);
    }

}
