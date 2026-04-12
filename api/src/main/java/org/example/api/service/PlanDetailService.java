package org.example.api.service;

import org.example.api.dto.AIPlanResponseDTO;
import org.example.api.dto.WorkoutPlanCourseDto;
import org.example.api.dto.WorkoutPlanDetailDto;
import org.example.api.entity.PlanDetail;

import java.time.LocalDate;
import java.util.List;

public interface PlanDetailService {
    /**
     * 保存训练详情
     * @param dayNumber 第几天
     * @param orderInDay 当天第几个动作
     * @param videoTitle 视频标题
     * @param workoutPlanId 关联的锻炼计划
     * */
    PlanDetail save(Integer dayNumber, Integer orderInDay, Long workoutPlanId, String videoTitle);


    /**
     * 批量保存AI生成的训练详情（仅保存一个训练计划中的所有训练详情）
     * */
    List<PlanDetail> saveAllByAIPlan(Long workoutPlanId, List<AIPlanResponseDTO.DayDetail> planDetails);

    /**
     * 获取训练计划详情
     */
    WorkoutPlanDetailDto getWorkoutPlanDetail(Long workoutPlanId);

    /**
     * 获取当前用户某天的训练详情列表
     */
    List<WorkoutPlanCourseDto> getTodayWorkoutPlanCourses(LocalDate date);
}
