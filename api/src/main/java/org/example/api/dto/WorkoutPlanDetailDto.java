package org.example.api.dto;

import lombok.Data;

import java.util.List;

@Data
public class WorkoutPlanDetailDto {
    private Long planId;
    private String planName;
    private Integer totalCourseCount;
    private List<WorkoutPlanCourseDto> courseList;
}
