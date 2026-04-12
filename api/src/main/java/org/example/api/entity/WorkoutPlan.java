package org.example.api.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 锻炼计划
 * */
@EqualsAndHashCode(callSuper = true)
@Entity
@Data
public class WorkoutPlan extends BaseEntity<Long> {
    @Column(name = "name")
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = true)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "form_id", nullable = true)
    private FitnessForm fitnessForm;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @CreationTimestamp
    @Column(name = "create_time", updatable = false)
    private LocalDateTime createTime;

    @OneToMany(mappedBy = "workoutPlan", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PlanDetail> planDetails = new ArrayList<>();

    public  WorkoutPlan() {
    }

    public WorkoutPlan(Long workoutPlanId) {
        this.id = workoutPlanId;
    }
}
