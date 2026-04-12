package org.example.api.dto;

import lombok.Data;

import java.util.List;

@Data
public class WorkoutPlanCourseDto {
    private String courseId;
    private Long planId;
    private String courseName;
    private List<WorkoutPlanActionDto> actionList;
    private Integer duration;
    private String difficulty;
    private Boolean learned;
    private String videoUrl;
    private String coverUrl;
}
