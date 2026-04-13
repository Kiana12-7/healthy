package org.example.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class WorkoutDurationSummaryDTO {
    private Integer totalDurationSeconds;
    private Integer planDurationSeconds;
    private Integer aiPlanDurationSeconds;
    private Integer activeDays;
    private List<WorkoutDurationDailyDTO> dailyRecords;
}
