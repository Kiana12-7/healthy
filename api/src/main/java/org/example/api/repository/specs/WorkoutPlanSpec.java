package org.example.api.repository.specs;

import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Path;
import org.example.api.entity.FitnessForm;
import org.example.api.entity.User;
import org.example.api.entity.WorkoutPlan;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class WorkoutPlanSpec {
    /**
     * 查询拥有此User的WorkoutPlan
     */
    public static Specification<WorkoutPlan> isUser(User user) {
        if (null == user) {
            return Specification.unrestricted();
        }
        return isUser(user.getId());
    }

    public static Specification<WorkoutPlan> isUser(Long userId) {
        if (null == userId) {
            return Specification.unrestricted();
        }
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder
                .equal(root.join("user", JoinType.LEFT).get("id").as(Long.class), userId);
    }

    public static Specification<WorkoutPlan> isDate(LocalDate date) {
        if (date == null) {
            return Specification.unrestricted();
        }
        return (root, query, cb) -> {
            Path<LocalDate> startDate = root.get("startDate");
            Path<LocalDate> endDate   = root.get("endDate");

            // startDate <= date AND endDate >= date
            return cb.and(
                    cb.lessThanOrEqualTo(startDate, date),
                    cb.greaterThanOrEqualTo(endDate, date)
            );
        };
    }
  }
