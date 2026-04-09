package org.example.api.controller;

import org.example.api.service.WorkoutPlanService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/vita")
public class AITalkController {
    private final WorkoutPlanService workoutPlanService;

    public AITalkController(WorkoutPlanService workoutPlanService) {
        this.workoutPlanService = workoutPlanService;
    }

    @GetMapping("/generatePlan")
    public void generatePlan() {
        this.workoutPlanService.generatePlan();
    }
}
