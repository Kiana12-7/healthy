package org.example.api.controller;

import org.example.api.dto.WorkoutDurationRecordRequestDTO;
import org.example.api.dto.WorkoutDurationSummaryDTO;
import org.example.api.service.WorkoutDurationService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/workoutDuration")
public class WorkoutDurationController {
    private final WorkoutDurationService workoutDurationService;

    public WorkoutDurationController(WorkoutDurationService workoutDurationService) {
        this.workoutDurationService = workoutDurationService;
    }

    @PostMapping("/record")
    public void recordDuration(@RequestBody WorkoutDurationRecordRequestDTO requestDTO) {
        workoutDurationService.recordDuration(requestDTO);
    }

    @GetMapping("/summary")
    public WorkoutDurationSummaryDTO getSummary(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        return workoutDurationService.getSummary(startDate, endDate);
    }
}
