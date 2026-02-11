package com.ead.course.specifications;


import com.ead.course.dtos.CourseFilterDto;
import com.ead.course.models.CourseModel;
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

}
