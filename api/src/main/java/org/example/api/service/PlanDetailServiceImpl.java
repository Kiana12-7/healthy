package org.example.api.service;

import jakarta.persistence.EntityNotFoundException;
import org.example.api.dto.AIPlanResponseDTO;
import org.example.api.dto.WorkoutPlanActionDto;
import org.example.api.dto.WorkoutPlanCourseDto;
import org.example.api.dto.WorkoutPlanDetailDto;
import org.example.api.entity.PlanDetail;
import org.example.api.entity.User;
import org.example.api.entity.Video;
import org.example.api.entity.WorkoutPlan;
import org.example.api.repository.PlanDetailRepository;
import org.example.api.repository.VideoRepository;
import org.example.api.repository.WorkoutPlanRepository;
import org.example.api.repository.specs.WorkoutPlanSpec;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

@Service
public class PlanDetailServiceImpl implements PlanDetailService{
    private final VideoRepository videoRepository;
    private final PlanDetailRepository planDetailRepository;
    private final WorkoutPlanRepository workoutPlanRepository;
    private final UserService userService;

    public PlanDetailServiceImpl(VideoRepository videoRepository,
                                 PlanDetailRepository planDetailRepository,
                                 WorkoutPlanRepository workoutPlanRepository,
                                 UserService userService) {
        this.videoRepository = videoRepository;
        this.planDetailRepository = planDetailRepository;
        this.workoutPlanRepository = workoutPlanRepository;
        this.userService = userService;
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

    @Override
    @Transactional(readOnly = true)
    public WorkoutPlanDetailDto getWorkoutPlanDetail(Long workoutPlanId) {
        WorkoutPlan workoutPlan = workoutPlanRepository.findById(workoutPlanId)
                .orElseThrow(EntityNotFoundException::new);
        List<PlanDetail> planDetails = planDetailRepository
                .findAllByWorkoutPlan_IdOrderByDayNumberAscOrderInDayAsc(workoutPlanId);

        Map<Integer, List<PlanDetail>> groupedByDay = planDetails.stream()
                .collect(Collectors.groupingBy(
                        PlanDetail::getDayNumber,
                        LinkedHashMap::new,
                        Collectors.toList()
                ));

        List<WorkoutPlanCourseDto> courseList = new ArrayList<>();
        groupedByDay.forEach((dayNumber, dailyPlanDetails) ->
                courseList.add(toCourseDto(workoutPlan, dayNumber, dailyPlanDetails))
        );

        WorkoutPlanDetailDto dto = new WorkoutPlanDetailDto();
        dto.setPlanId(workoutPlan.getId());
        dto.setPlanName(workoutPlan.getName());
        dto.setTotalCourseCount(courseList.size());
        dto.setCourseList(courseList);
        return dto;
    }

    private WorkoutPlanCourseDto toCourseDto(WorkoutPlan workoutPlan, Integer dayNumber, List<PlanDetail> dailyPlanDetails) {
        List<PlanDetail> sortedDetails = dailyPlanDetails.stream()
                .sorted(Comparator.comparing(PlanDetail::getOrderInDay))
                .collect(Collectors.toList());

        List<WorkoutPlanActionDto> actionList = sortedDetails.stream()
                .map(this::toActionDto)
                .collect(Collectors.toList());

        Video firstVideo = sortedDetails.stream()
                .map(PlanDetail::getVideo)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);

        WorkoutPlanCourseDto dto = new WorkoutPlanCourseDto();
        dto.setCourseId(workoutPlan.getId() + "_" + dayNumber);
        dto.setPlanId(workoutPlan.getId());
        dto.setPlanName(workoutPlan.getName());
        dto.setCourseName("第" + dayNumber + "天训练");
        dto.setActionList(actionList);
        dto.setDuration(resolveCourseDuration(sortedDetails));
        dto.setDifficulty(resolveDifficulty(workoutPlan.getName()));
        dto.setLearned(false);
        dto.setVideoUrl(firstVideo != null && firstVideo.getUrl() != null ? firstVideo.getUrl() : "");
        dto.setCoverUrl(firstVideo != null ? firstVideo.getCoverUrl() : null);
        return dto;
    }

    private WorkoutPlanActionDto toActionDto(PlanDetail planDetail) {
        Video video = planDetail.getVideo();
        String actionName = video != null && video.getTitle() != null ? video.getTitle() : "训练动作";

        WorkoutPlanActionDto dto = new WorkoutPlanActionDto();
        dto.setActionId(video != null && video.getId() != null ? video.getId() : planDetail.getId());
        dto.setActionName(actionName);
        dto.setGroupDesc(resolveGroupDesc(actionName));
        dto.setRestDesc("组间休息：45秒");
        dto.setVideoUrl(video != null && video.getUrl() != null ? video.getUrl() : "");
        dto.setActionDesc(buildActionDesc(actionName, video != null ? video.getDuration() : null));
        return dto;
    }

    private Integer resolveCourseDuration(List<PlanDetail> planDetails) {
        int totalVideoMinutes = (int) Math.ceil(
                planDetails.stream()
                        .map(PlanDetail::getVideo)
                        .filter(Objects::nonNull)
                        .map(Video::getDuration)
                        .filter(Objects::nonNull)
                        .mapToInt(Integer::intValue)
                        .sum() / 60.0
        );

        if (totalVideoMinutes > 0) {
            return Math.max(totalVideoMinutes, planDetails.size() * 3);
        }
        return Math.max(10, planDetails.size() * 5);
    }

    private String resolveDifficulty(String planName) {
        if (containsAny(planName, "冲刺", "强化", "马拉松", "撕裂")) {
            return "强化";
        }
        if (containsAny(planName, "增肌", "跑步", "跳绳", "搏击", "体测")) {
            return "进阶";
        }
        return "基础";
    }

    private String resolveGroupDesc(String actionName) {
        if ("平板支撑".equals(actionName)) {
            return "45秒 × 4组";
        }
        if ("卷腹".equals(actionName)) {
            return "20次 × 4组";
        }
        if ("引体向上".equals(actionName)) {
            return "8次 × 4组";
        }
        return "12次 × 4组";
    }

    private String buildActionDesc(String actionName, Integer videoDuration) {
        String durationText = (videoDuration != null && videoDuration > 0)
                ? "示范视频约 " + videoDuration + " 秒。"
                : "";
        return "跟随视频完成「" + actionName + "」动作，保持核心收紧、呼吸稳定，优先保证动作标准。" + durationText;
    }

    private boolean containsAny(String text, String... keywords) {
        for (String keyword : keywords) {
            if (text.contains(keyword)) {
                return true;
            }
        }
        return false;
    }

    @Override
    @Transactional(readOnly = true)
    public List<WorkoutPlanCourseDto> getTodayWorkoutPlanCourses(LocalDate date) {
        User currentUser = userService.getCurrentLoginUserDetails();
        Specification<WorkoutPlan> spec = WorkoutPlanSpec.isDate(date).and(WorkoutPlanSpec.isUser(currentUser));
        List<WorkoutPlan> workoutPlans = workoutPlanRepository.findAll(spec);

        List<WorkoutPlanCourseDto> result = new ArrayList<>();

            int dayNumber = (int) ChronoUnit.DAYS.between(workoutPlans.get(0).getStartDate(), date) + 1;
            if (dayNumber >= 1) {
                List<PlanDetail> dailyPlanDetails = planDetailRepository
                        .findAllByWorkoutPlan_IdAndDayNumberOrderByOrderInDayAsc(workoutPlans.get(0).getId(), dayNumber);
                if (!dailyPlanDetails.isEmpty()) {
                    result.add(toCourseDto(workoutPlans.get(0), dayNumber, dailyPlanDetails));
                }
            }

        return result;
    }
}
