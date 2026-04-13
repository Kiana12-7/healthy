package org.example.api.dto;

import lombok.Data;
import org.example.api.enums.WorkoutDurationSourceType;

import java.time.LocalDate;

@Data
public class WorkoutDurationRecordRequestDTO {
    private LocalDate recordDate;
    private Integer durationSeconds;
    private WorkoutDurationSourceType sourceType;
}
