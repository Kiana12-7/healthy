package org.example.api.service;

import org.example.api.dto.WorkoutDurationRecordRequestDTO;
import org.example.api.dto.WorkoutDurationSummaryDTO;

import java.time.LocalDate;

public interface WorkoutDurationService {
    void recordDuration(WorkoutDurationRecordRequestDTO requestDTO);

    WorkoutDurationSummaryDTO getSummary(LocalDate startDate, LocalDate endDate);
}
