package org.example.api.service;

import org.example.api.dto.CurrentWorkoutPlanSummaryDTO;
import org.example.api.dto.WorkoutPlanListDto;
import org.example.api.entity.WorkoutPlan;

import java.time.LocalDate;
import java.util.List;

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

    /**
     * 保存锻炼计划
     * @param startTime 开始日期
     * @param endTime  结束日期
     * @param fitnessFormId 关联的用户健康表
     * */
    WorkoutPlan save(LocalDate startTime, LocalDate endTime, Long fitnessFormId, String name);

    /**
     * 获取首页展示用的内置训练计划
     */
    List<WorkoutPlanListDto> getTemplatePlanList();

    CurrentWorkoutPlanSummaryDTO getCurrentPlanSummary(LocalDate date);

}
