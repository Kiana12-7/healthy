package org.example.api.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class WorkoutPlanListDto {
    private Long id;
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
}
