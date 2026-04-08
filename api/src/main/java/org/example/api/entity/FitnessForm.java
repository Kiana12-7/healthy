package org.example.api.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户信息表单
 */
@EqualsAndHashCode(callSuper = true)
@Entity
@Data
public class FitnessForm extends BaseEntity<Long> {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "part_state", nullable = false)
    private byte partState;

    @Column(length = 10)
    private String height;

    @Column(name = "train_location")
    private byte trainLocation;

    @Column(name = "body_type")
    private byte bodyType;

    @Column(name = "hurt_location")
    private byte hurtLocation;

    @Column(name = "plan_type")
    private byte planType;

    @Column(name = "passion_sport")
    private byte passionSport;

    @UpdateTimestamp
    @Column(name = "update_time", updatable = false)
    private LocalDateTime updateTime;

    // 与 WorkoutPlan 的一对多关系
    @OneToMany(mappedBy = "fitnessForm", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<WorkoutPlan> workoutPlans = new ArrayList<>();

}
