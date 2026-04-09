package org.example.api.service;

import org.example.api.dto.AIPlanResponseDTO;
import org.example.api.entity.PlanDetail;
import org.example.api.entity.Video;
import org.example.api.entity.WorkoutPlan;
import org.example.api.repository.PlanDetailRepository;
import org.example.api.repository.VideoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlanDetailServiceImpl implements PlanDetailService{
    private final VideoRepository videoRepository;
    private final PlanDetailRepository planDetailRepository;

    public PlanDetailServiceImpl(VideoRepository videoRepository, PlanDetailRepository planDetailRepository) {
        this.videoRepository = videoRepository;
        this.planDetailRepository = planDetailRepository;
    }

    @Override
    public PlanDetail save(Integer dayNumber, Integer orderInDay, Long workoutPlanId, String videoTitle) {
        PlanDetail planDetail = new PlanDetail();
        planDetail.setDayNumber(dayNumber);
        planDetail.setWorkoutPlan(new WorkoutPlan(workoutPlanId));
        planDetail.setOrderInDay(orderInDay);
        Video video = this.videoRepository.findByTitle(videoTitle);
        planDetail.setVideo(video);
        return this.planDetailRepository.save(planDetail);
    }

    @Override
    public List<PlanDetail> saveAllByAIPlan(Long workoutPlanId, List<AIPlanResponseDTO.DayDetail> planDetails) {
        List<PlanDetail> result = new java.util.ArrayList<>(List.of());
        // 调用save方法
        planDetails.forEach((planDetail) -> {
            planDetail.getVideos().forEach((video) -> {
                PlanDetail planDetail1 = this.save(planDetail.getDayNumber(), video.getOrderInDay(), workoutPlanId, video.getActionName());
                result.add(planDetail1);
            });
        });
        return result;
    }
}
