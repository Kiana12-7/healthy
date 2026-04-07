package org.example.api.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *
 * ai计划详情
 * */
@EqualsAndHashCode(callSuper = true)
@Entity
@Data
public class PlanDetail extends BaseEntity<Long> {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id", nullable = false)
    private WorkoutPlan workoutPlan;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "video_id")
    private Video video;

    @Column(name = "day_number", nullable = false)
    private Integer dayNumber;      // 第几天

    @Column(name = "order_in_day", nullable = false)
    private Integer orderInDay;     // 当天第几个动作
}
