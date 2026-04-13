package org.example.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class WorkoutDurationDailyDTO {
    private LocalDate recordDate;
    private Integer planDurationSeconds;
    private Integer aiPlanDurationSeconds;
    private Integer totalDurationSeconds;
}
