package com.ead.course.services.impl;

import com.ead.course.dtos.ModuleDto;
import com.ead.course.dtos.ModuleFilterDto;
import com.ead.course.models.CourseModel;
import com.ead.course.models.ModuleModel;
import com.ead.course.repositories.ModuleRepository;
import com.ead.course.services.ModuleService;
import com.ead.course.specifications.Specifications;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

@Slf4j
@Service
public class ModuleServiceImpl implements ModuleService {

    private final ModuleRepository moduleRepository;
    private final ModuleService moduleService;

    public ModuleServiceImpl(ModuleRepository moduleRepository, @Lazy ModuleService moduleService) {
        this.moduleRepository = moduleRepository;
        this.moduleService = moduleService;
    }

    @Override
    public void deleteById(UUID moduleId) {
        log.info("Deleting module - moduleId: {}", moduleId);
        int deletedCount = moduleRepository.deleteModuleById(moduleId);
        if (deletedCount <= 0) {
            log.warn("Module not found for delete - moduleId: {}", moduleId);
            throw new RuntimeException("Module not found with ID: " + moduleId);
        }
        log.info("Module deleted successfully - moduleId: {}", moduleId);
    }

    @Override
    @Transactional
    public ModuleModel save(CourseModel course, ModuleDto moduleDto) {
        log.info("Saving module - courseId: {}, title: {}", course.getCourseId(), moduleDto.title());
        var moduleModel = new ModuleModel();
        BeanUtils.copyProperties(moduleDto, moduleModel, "moduleId");
        moduleModel.setCourse(course);
        moduleModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        var savedModule = moduleRepository.save(moduleModel);
        log.info("Module saved successfully - moduleId: {}, courseId: {}", savedModule.getModuleId(),
                course.getCourseId());
        return savedModule;
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "modules", key = "{#course.courseId, #filter.title, #filter.description, #pageable.pageNumber," +
            " #pageable.pageSize, #pageable.sort}")
    public Page<ModuleModel> getAllModulesByCourse(CourseModel course, ModuleFilterDto filter, Pageable pageable) {
        log.info("Finding modules - courseId: {}, filter: {}, pageable: {}", course.getCourseId(), filter, pageable);
        Specification<ModuleModel> spec = Specifications.moduleFilters(filter)
                .and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("course"), course));
        return moduleRepository.findAll(spec, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "modules", key = "#moduleId")
    public ModuleModel findById(CourseModel course, UUID moduleId) {
        log.info("Finding module - courseId: {}, moduleId: {}", course.getCourseId(), moduleId);
        return moduleRepository.findById(moduleId)
                .filter(module -> module.getCourse().getCourseId().equals(course.getCourseId()))
                .orElseThrow(() -> new RuntimeException("Module not found with ID: " + moduleId + " for Course ID: " + course.getCourseId() + " or module is not associated with the course"));
    }

    @Override
    @Transactional
    @CacheEvict(value = "modules", key = "#moduleId")
    public ModuleModel updateModule(CourseModel course, UUID moduleId, ModuleDto moduleDto) {
        log.info("Updating module - courseId: {}, moduleId: {}, title: {}", course.getCourseId(), moduleId,
                moduleDto.title());
        var moduleModel = moduleService.findById(course, moduleId);
        BeanUtils.copyProperties(moduleDto, moduleModel, "moduleId", "creationDate", "course");
        var savedModule = moduleRepository.save(moduleModel);
        log.info("Module updated successfully - moduleId: {}, courseId: {}", savedModule.getModuleId(),
                course.getCourseId());
        return savedModule;
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "modules", key = "#moduleId")
    public ModuleModel findById(UUID moduleId) {
        log.info("Finding module by id: {}", moduleId);
        return moduleRepository.findById(moduleId).orElseThrow(() -> new RuntimeException("Module not found with ID: "
                + moduleId));
    }

}
