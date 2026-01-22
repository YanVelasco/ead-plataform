package com.ead.authuser.specifications;

import com.ead.authuser.dtos.UserFilterDto;
import com.ead.authuser.model.UserModel;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

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

            if (filter.username() != null && !filter.username().isBlank()) {
                predicates.add(
                        criteriaBuilder.like(
                                criteriaBuilder.lower(root.get("username")),
                                "%" + filter.username().toLowerCase() + "%"
                        )
                );
            }

            if (filter.userType() != null) {
                predicates.add(
                        criteriaBuilder.equal(root.get("userType"), filter.userType())
                );
            }

            if (filter.userStatus() != null) {
                predicates.add(
                        criteriaBuilder.equal(root.get("userStatus"), filter.userStatus())
                );
            }

            if(filter.fullName() != null && !filter.fullName().isBlank()) {
                predicates.add(
                        criteriaBuilder.like(
                                criteriaBuilder.lower(root.get("fullName")),
                                "%" + filter.fullName().toLowerCase() + "%"
                        )
                );
            }

            if (filter.email() != null && !filter.email().isBlank()) {
                predicates.add(
                        criteriaBuilder.like(
                                criteriaBuilder.lower(root.get("email")),
                                "%" + filter.email().toLowerCase() + "%"
                        )
                );
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

}
