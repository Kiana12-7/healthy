package org.example.api.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class CurrentWorkoutPlanSummaryDTO {
    private boolean hasActivePlan;
    private Long planId;
    private String planName;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer currentDay;
    private Integer totalDays;
}
