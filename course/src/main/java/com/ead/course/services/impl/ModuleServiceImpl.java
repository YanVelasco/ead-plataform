package com.ead.course.services.impl;

import com.ead.course.repositories.ModuleRepository;
import com.ead.course.services.ModuleService;
import org.springframework.stereotype.Service;

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

}
