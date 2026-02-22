package com.ead.course.specifications;


import com.ead.course.dtos.CourseFilterDto;
import com.ead.course.dtos.LessonFilter;
import com.ead.course.dtos.ModuleFilterDto;
import com.ead.course.models.CourseModel;
import com.ead.course.models.LessonModel;
import com.ead.course.models.ModuleModel;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class Specifications {

    private Specifications() {
        throw new IllegalStateException("Utility class");
    }

    public static Specification<CourseModel> courseFilters(CourseFilterDto filter) {
        return (root, query, criteriaBuilder) -> {
            if (filter == null) {
                return criteriaBuilder.conjunction();
            }

            List<Predicate> predicates = new ArrayList<>();

            if (filter.name() != null && !filter.name().isBlank()) {
                predicates.add(
                        criteriaBuilder.like(
                                criteriaBuilder.lower(root.get("name")),
                                "%" + filter.name().toLowerCase() + "%"
                        )
                );
            }

            if (filter.description() != null) {
                predicates.add(
                        criteriaBuilder.like(
                                criteriaBuilder.lower(root.get("description")),
                                "%" + filter.description().toLowerCase() + "%"
                        )
                );
            }

            if (filter.courseStatus() != null) {
                predicates.add(
                        criteriaBuilder.equal(root.get("courseStatus"), filter.courseStatus())
                );
            }

            if (filter.courseLevel() != null) {
                predicates.add(
                        criteriaBuilder.equal(root.get("courseLevel"), filter.courseLevel())
                );
            }

            if (filter.userInstructor() != null) {
                predicates.add(
                        criteriaBuilder.like(
                                criteriaBuilder.lower(root.get("userInstructor")),
                                "%" + filter.userInstructor().toString().toLowerCase() + "%"
                        )
                );
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    public static Specification<ModuleModel> moduleFilters(ModuleFilterDto filter,  CourseModel course) {
        return (root, query, criteriaBuilder) -> {
            if (filter == null) {
                return criteriaBuilder.conjunction();
            }

            List<Predicate> predicates = new ArrayList<>();

            if(filter.title() != null && !filter.title().isBlank()) {
                predicates.add(
                        criteriaBuilder.like(
                                criteriaBuilder.lower(root.get("title")),
                                "%" + filter.title().toLowerCase() + "%"
                        )
                );
            }

            if (filter.description() != null && !filter.description().isBlank()) {
                predicates.add(
                        criteriaBuilder.like(
                                criteriaBuilder.lower(root.get("description")),
                                "%" + filter.description().toLowerCase() + "%"
                        )
                );
            }

            if (course != null) {
                predicates.add(
                        criteriaBuilder.equal(root.get("course"), course)
                );
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    public static Specification<LessonModel> lessonFilters(LessonFilter filter, ModuleModel module) {
        return (root, query, criteriaBuilder) -> {
            if (filter == null) {
                return criteriaBuilder.conjunction();
            }

            if (filter.title() != null && !filter.title().isBlank()) {
                return criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("title")),
                        "%" + filter.title().toLowerCase() + "%"
                );
            }

            if (filter.description() != null && !filter.description().isBlank()) {
                return criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("description")),
                        "%" + filter.description().toLowerCase() + "%"
                );
            }

            if (module != null) {
                return criteriaBuilder.equal(root.get("module"), module);
            }

            return criteriaBuilder.conjunction();
        };
    }

}
