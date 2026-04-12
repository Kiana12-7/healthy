package org.example.api.dto;

import lombok.Data;

@Data
public class WorkoutPlanActionDto {
    private Long actionId;
    private String actionName;
    private String groupDesc;
    private String restDesc;
    private String videoUrl;
    private String actionDesc;
}
