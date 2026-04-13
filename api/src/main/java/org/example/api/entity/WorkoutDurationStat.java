package org.example.api.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Table(
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_workout_duration_stat_user_date", columnNames = {"user_id", "record_date"})
        },
        indexes = {
                @Index(name = "idx_workout_duration_stat_user_date", columnList = "user_id,record_date")
        }
)
public class WorkoutDurationStat extends BaseEntity<Long> {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "record_date", nullable = false)
    private LocalDate recordDate;

    @Column(name = "plan_duration_seconds", nullable = false)
    private Integer planDurationSeconds = 0;

    @Column(name = "ai_plan_duration_seconds", nullable = false)
    private Integer aiPlanDurationSeconds = 0;

    @Column(name = "total_duration_seconds", nullable = false)
    private Integer totalDurationSeconds = 0;
}
