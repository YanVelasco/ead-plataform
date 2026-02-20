package com.ead.course.services.impl;

import com.ead.course.dtos.CourseDto;
import com.ead.course.dtos.CourseFilterDto;
import com.ead.course.exceptions.AlreadyExistsException;
import com.ead.course.exceptions.NotFoundException;
import com.ead.course.models.CourseModel;
import com.ead.course.repositories.CourseRepository;
import com.ead.course.services.CourseService;
import com.ead.course.specifications.Specifications;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
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
        log.info("Deleting course - courseId: {}", courseId);
        this.getById(courseId);
        courseRepository.deleteById(courseId);
        log.info("Course deleted successfully - courseId: {}", courseId);
    }

    @Override
    @Transactional
    public CourseModel save(CourseDto courseDto) {
        log.info("Saving course - name: {}", courseDto.name());
        CourseModel courseModel = new CourseModel();
        BeanUtils.copyProperties(courseDto, courseModel);
        courseModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        courseModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
        var savedCourse = courseRepository.save(courseModel);
        log.info("Course saved successfully - courseId: {}", savedCourse.getCourseId());
        return savedCourse;
    }

    @Override
    public boolean existsByName(String courseName) {
        if (courseRepository.existsByName(courseName)) {
            log.warn("Course name already exists - name: {}", courseName);
            throw new AlreadyExistsException("Course with name '" + courseName + "' already exists.");
        }
        log.debug("Course name available - name: {}", courseName);
        return false;
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "courses", key = "{#filter.name, #filter.courseStatus, #filter.courseLevel, #filter" +
            ".description, #filter.userInstructor, #pageable.pageNumber, #pageable.pageSize, #pageable.sort}")
    public Page<CourseModel> getAllCourses(CourseFilterDto filter, Pageable pageable) {
        log.info("Finding courses - filter: {}, pageable: {}", filter, pageable);
        Specification<CourseModel> spec = Specifications.courseFilters(filter);
        return courseRepository.findAll(spec, pageable);
    }

    @Override
    public CourseModel getById(UUID courseId) {
        log.info("Finding course by id: {}", courseId);
        return courseRepository.findById(courseId).orElseThrow(() -> new NotFoundException("Course not found with ID:" +
                " " + courseId));
    }

    @Override
    @Transactional
    public CourseModel updateById(UUID courseId, CourseDto courseDto) {
        log.info("Updating course - courseId: {}, name: {}", courseId, courseDto.name());
        var courseModel = getById(courseId);
        BeanUtils.copyProperties(courseDto, courseModel, "id");
        courseModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
        var savedCourse = courseRepository.save(courseModel);
        log.info("Course updated successfully - courseId: {}", savedCourse.getCourseId());
        return savedCourse;
    }

}
