package com.ead.authuser.specifications;

import com.ead.authuser.dtos.UserFilterDto;
import com.ead.authuser.model.UserModel;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserSpecifications {

    private UserSpecifications() {
        throw new IllegalStateException("Utility class");
    }

    public static Specification<UserModel> withFilters(UserFilterDto filter) {
        return (root, query, criteriaBuilder) -> {
            if (filter == null) {
                return criteriaBuilder.conjunction();
            }

            List<Predicate> predicates = new ArrayList<>();

            addLikePredicate(predicates, criteriaBuilder, root, "username", filter.username());
            addEqualPredicate(predicates, criteriaBuilder, root, "userType", filter.userType());
            addEqualPredicate(predicates, criteriaBuilder, root, "userStatus", filter.userStatus());
            addLikePredicate(predicates, criteriaBuilder, root, "fullName", filter.fullName());
            addLikePredicate(predicates, criteriaBuilder, root, "email", filter.email());
            assert query != null;
            addCoursePredicate(predicates, criteriaBuilder, query, root, filter.courseId());

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    private static void addLikePredicate(
            List<Predicate> predicates,
            CriteriaBuilder criteriaBuilder,
            Root<UserModel> root,
            String field,
            String value
    ) {
        if (value == null || value.isBlank()) {
            return;
        }

        predicates.add(
                criteriaBuilder.like(
                        criteriaBuilder.lower(root.get(field)),
                        "%" + value.toLowerCase() + "%"
                )
        );
    }

    private static void addEqualPredicate(
            List<Predicate> predicates,
            CriteriaBuilder criteriaBuilder,
            Root<UserModel> root,
            String field,
            Object value
    ) {
        if (value == null) {
            return;
        }

        predicates.add(criteriaBuilder.equal(root.get(field), value));
    }

    private static void addCoursePredicate(
            List<Predicate> predicates,
            CriteriaBuilder criteriaBuilder,
            CriteriaQuery<?> query,
            Root<UserModel> root,
            UUID courseId
    ) {
        if (courseId == null) {
            return;
        }
        query.distinct(true);
        predicates.add(
                criteriaBuilder.equal(
                        root.join("userCourseModels").get("courseId"),
                        courseId
                )
        );
    }

}
