package org.example.api.repository.specs;

import jakarta.persistence.criteria.JoinType;
import org.example.api.entity.FitnessForm;
import org.example.api.entity.User;
import org.springframework.data.jpa.domain.Specification;

public class FitnessFormSpec {
    /**
     * 查询拥有此User的FitnessForm
     */
    public static Specification<FitnessForm> isUser(User user) {
        if (null == user) {
            return Specification.unrestricted();
        }
        return isUser(user.getId());
    }

    public static Specification<FitnessForm> isUser(Long userId) {
        if (null == userId) {
            return Specification.unrestricted();
        }
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder
                .equal(root.join("user", JoinType.LEFT).get("id").as(Long.class), userId);
    }
}
