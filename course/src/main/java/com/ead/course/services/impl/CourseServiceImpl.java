package com.ead.course.services.impl;

import com.ead.course.dtos.CourseDto;
import com.ead.course.dtos.CourseFilterDto;
import com.ead.course.exceptions.AlreadyExistsException;
import com.ead.course.exceptions.NotFoundException;
import com.ead.course.models.CourseModel;
import com.ead.course.repositories.CourseRepository;
import com.ead.course.services.CourseService;
import com.ead.course.specifications.Specifications;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

@Service
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;

    public CourseServiceImpl(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Override
    @Transactional
    @CacheEvict(value = "courses", key = "#courseId")
    public void delete(UUID courseId) {
        this.getById(courseId);
        courseRepository.deleteById(courseId);
    }

    @Override
    @Transactional
    public CourseModel save(CourseDto courseDto) {
        CourseModel courseModel = new CourseModel();
        BeanUtils.copyProperties(courseDto, courseModel);
        courseModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        courseModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
        return courseRepository.save(courseModel);
    }

    @Override
    public boolean existsByName(String courseName) {
        if (courseRepository.existsByName(courseName)) {
            throw new AlreadyExistsException("Course with name '" + courseName + "' already exists.");
        }
        return false;
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "courses", key = "{#filter.name, #filter.courseStatus, #filter.courseLevel, #filter" +
            ".description, #filter.userInstructor, #pageable.pageNumber, #pageable.pageSize, #pageable.sort}")
    public Page<CourseModel> getAllCourses(CourseFilterDto filter, Pageable pageable) {
        Specification<CourseModel> spec = Specifications.courseFilters(filter);
        return courseRepository.findAll(spec, pageable);
    }

    @Override
    public CourseModel getById(UUID courseId) {
        return courseRepository.findById(courseId).orElseThrow(() -> new NotFoundException("Course not found with ID:" +
                " " + courseId));
    }

    @Override
    @Transactional
    public CourseModel updateById(UUID courseId, CourseDto courseDto) {
        var courseModel = getById(courseId);
        BeanUtils.copyProperties(courseDto, courseModel, "id");
        courseModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
        return courseRepository.save(courseModel);
    }

}
