package com.ead.course.specifications;


import com.ead.course.dtos.CourseFilterDto;
import com.ead.course.dtos.LessonFilter;
import com.ead.course.dtos.ModuleFilterDto;
import com.ead.course.dtos.UserFilterDto;
import com.ead.course.models.CourseModel;
import com.ead.course.models.LessonModel;
import com.ead.course.models.ModuleModel;
import com.ead.course.models.UserModel;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

            addLikePredicate(predicates, criteriaBuilder, root.get("name"), filter.name());
            addLikePredicate(predicates, criteriaBuilder, root.get("description"), filter.description());
            addEqualPredicate(predicates, criteriaBuilder, root.get("courseStatus"), filter.courseStatus());
            addEqualPredicate(predicates, criteriaBuilder, root.get("courseLevel"), filter.courseLevel());
            addInstructorPredicate(predicates, criteriaBuilder, root, query, filter);

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    public static Specification<ModuleModel> moduleFilters(ModuleFilterDto filter, CourseModel course) {
        return (root, query, criteriaBuilder) -> {
            if (filter == null) {
                return criteriaBuilder.conjunction();
            }

            List<Predicate> predicates = new ArrayList<>();

            if (filter.title() != null && !filter.title().isBlank()) {
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

    private static void addLikePredicate(List<Predicate> predicates,
                                         CriteriaBuilder criteriaBuilder,
                                         Expression<String> path,
                                         String value) {
        if (hasText(value)) {
            predicates.add(
                    criteriaBuilder.like(
                            criteriaBuilder.lower(path),
                            "%" + value.toLowerCase() + "%"
                    )
            );
        }
    }

    private static void addEqualPredicate(List<Predicate> predicates,
                                          CriteriaBuilder criteriaBuilder,
                                          Expression<?> path,
                                          Object value) {
        if (value != null) {
            predicates.add(criteriaBuilder.equal(path, value));
        }
    }

    private static void addInstructorPredicate(List<Predicate> predicates,
                                               CriteriaBuilder criteriaBuilder,
                                               Root<CourseModel> root,
                                               CriteriaQuery<?> query,
                                               CourseFilterDto filter) {
        if (filter.userInstructor() == null) {
            return;
        }
        if (query != null) {
            query.distinct(true);
        }
        predicates.add(
                criteriaBuilder.equal(
                        root.join("users").get("userId"),
                        filter.userInstructor()
                )
        );
    }

    public static Specification<UserModel> userFilters(UserFilterDto filter, UUID courseId) {
        return (root, query, criteriaBuilder) -> {
            if (filter == null) {
                return criteriaBuilder.conjunction();
            }

            List<Predicate> predicates = new ArrayList<>();

            addLikePredicate(predicates, criteriaBuilder, root.get("fullName"), filter.fullName());
            addLikePredicate(predicates, criteriaBuilder, root.get("email"), filter.email());
            addEqualPredicate(predicates, criteriaBuilder, root.get("userStatus"), filter.userStatus());
            addEqualPredicate(predicates, criteriaBuilder, root.get("userType"), filter.userType());
            addCourseIdFilter(predicates, criteriaBuilder, root, query, courseId);

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    private static void addCourseIdFilter(List<Predicate> predicates,
                                          CriteriaBuilder criteriaBuilder,
                                          Root<UserModel> root,
                                          CriteriaQuery<?> query,
                                          UUID courseId) {
        if (query != null) {
            query.distinct(true);
        }
        predicates.add(
                criteriaBuilder.equal(
                        root.join("courses").get("courseId"),
                        courseId
                )
        );
    }

    private static boolean hasText(String value) {
        return value != null && !value.isBlank();
    }

}
