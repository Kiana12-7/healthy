package org.example.api.controller;

import org.example.api.dto.WorkoutPlanDetailDto;
import org.example.api.dto.WorkoutPlanListDto;
import org.example.api.service.PlanDetailService;
import org.example.api.service.WorkoutPlanService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/workoutPlan")
public class WorkoutPlanController {
    private final PlanDetailService planDetailService;
    private final WorkoutPlanService workoutPlanService;

    public WorkoutPlanController(PlanDetailService planDetailService, WorkoutPlanService workoutPlanService) {
        this.planDetailService = planDetailService;
        this.workoutPlanService = workoutPlanService;
    }

    @GetMapping("/list")
    public List<WorkoutPlanListDto> getWorkoutPlanList() {
        return workoutPlanService.getTemplatePlanList();
    }

    @GetMapping("/{planId}/detail")
    public WorkoutPlanDetailDto getWorkoutPlanDetail(@PathVariable Long planId) {
        return planDetailService.getWorkoutPlanDetail(planId);
    }
}
