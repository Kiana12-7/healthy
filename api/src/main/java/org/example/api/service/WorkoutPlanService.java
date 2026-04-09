package org.example.api.service;

import org.example.api.entity.WorkoutPlan;

import java.time.LocalDate;

public interface WorkoutPlanService {
    /**
     * 生成AI计划
     * */
    WorkoutPlan generatePlan();

    /**
     * 保存锻炼计划
     * @param startTime 开始日期
     * @param endTime  结束日期
     * @param fitnessFormId 关联的用户健康表
     * */
    WorkoutPlan save(LocalDate startTime, LocalDate endTime, Long fitnessFormId);
}
