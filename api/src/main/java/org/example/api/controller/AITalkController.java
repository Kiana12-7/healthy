package org.example.api.controller;

import org.example.api.dto.AIChatRequestDTO;
import org.example.api.dto.AIChatResponseDTO;
import org.example.api.service.AIChatService;
import org.example.api.service.WorkoutPlanService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/vita")
public class AITalkController {
    private final AIChatService aiChatService;
    private final WorkoutPlanService workoutPlanService;

    public AITalkController(AIChatService aiChatService, WorkoutPlanService workoutPlanService) {
        this.aiChatService = aiChatService;
        this.workoutPlanService = workoutPlanService;
    }

    @GetMapping("/generatePlan")
    public void generatePlan() {
        this.workoutPlanService.generatePlan();
    }

    @PostMapping("/chat")
    public AIChatResponseDTO chat(@RequestBody AIChatRequestDTO request) {
        return new AIChatResponseDTO(aiChatService.ask(request.getMessage()));
    }
}
