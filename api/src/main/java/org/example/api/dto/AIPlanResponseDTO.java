package org.example.api.dto;

import lombok.Data;

import java.util.List;

public class AIPlanResponseDTO {
    private String planType;
    private Integer durationDays;
    private String startDate;
    private String endDate;
    private Integer weeklyFrequency;
    private List<DayDetail> details;

    @Data
    public static class DayDetail {
        private Integer dayNumber;
        private List<Video> videos;
    }

    @Data
    public static class Video {
        private String actionName;   // AI 返回动作名称
        private Integer orderInDay;
    }
}
