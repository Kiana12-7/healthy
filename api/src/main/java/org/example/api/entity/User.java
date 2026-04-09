package org.example.api.entity;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户实体
 */
@EqualsAndHashCode(callSuper = true)
@Entity
@Data
public class User extends BaseEntity<Long> {
    private String username;

    private String name;

    private String phone;

    @JsonView(PasswordJsonView.class)
    private String password;

    // 与 FitnessForm 的一对一关系
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private FitnessForm fitnessForm;

    // 与 WorkoutPlan 的一对多关系
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<WorkoutPlan> workoutPlans = new ArrayList<>();

    public interface PasswordJsonView {
    }
}

