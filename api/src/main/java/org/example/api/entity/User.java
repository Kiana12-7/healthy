package org.example.api.entity;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * 用户实体
 * */
@EqualsAndHashCode(callSuper = true)
@Entity
@Data
public class User extends club.yunzhi.minicrm.entity.BaseEntity<Long> {
    private String username;

    private String name;

    private String phone;

    @JsonView(PasswordJsonView.class)
    private String password;

    // 与 FitnessForm 的一对多关系（可选）
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<FitnessForm> fitnessForms = new ArrayList<>();

    // 与 WorkoutPlan 的一对多关系（可选）
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<WorkoutPlan> workoutPlans = new ArrayList<>();

    public void setUsername(String username) {
        this.setPhone(username);
    }

    public interface PasswordJsonView {
    }
}

