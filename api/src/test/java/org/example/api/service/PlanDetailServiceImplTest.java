package org.example.api.service;

import org.example.api.dto.WorkoutPlanDetailDto;
import org.example.api.entity.PlanDetail;
import org.example.api.entity.Video;
import org.example.api.entity.WorkoutPlan;
import org.example.api.repository.PlanDetailRepository;
import org.example.api.repository.VideoRepository;
import org.example.api.repository.WorkoutPlanRepository;
import org.example.api.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PlanDetailServiceImplTest {

    @Mock
    private VideoRepository videoRepository;

    @Mock
    private PlanDetailRepository planDetailRepository;

    @Mock
    private WorkoutPlanRepository workoutPlanRepository;

    @Mock
    private UserService userService;

    private PlanDetailServiceImpl planDetailService;

    @BeforeEach
    void setUp() {
        planDetailService = new PlanDetailServiceImpl(videoRepository, planDetailRepository, workoutPlanRepository, userService);
    }

    @Test
    void getWorkoutPlanDetailGroupsActionsByDay() {
        WorkoutPlan workoutPlan = new WorkoutPlan();
        workoutPlan.setId(1L);
        workoutPlan.setName("个性减脂计划");

        Video squat = new Video();
        squat.setId(11L);
        squat.setTitle("深蹲");
        squat.setUrl("https://example.com/squat.mp4");
        squat.setDuration(120);
        squat.setCoverUrl("https://example.com/squat.jpg");

        Video plank = new Video();
        plank.setId(12L);
        plank.setTitle("平板支撑");
        plank.setUrl("https://example.com/plank.mp4");
        plank.setDuration(90);
        plank.setCoverUrl("https://example.com/plank.jpg");

        PlanDetail dayOneActionOne = new PlanDetail();
        dayOneActionOne.setId(101L);
        dayOneActionOne.setDayNumber(1);
        dayOneActionOne.setOrderInDay(1);
        dayOneActionOne.setVideo(squat);

        PlanDetail dayOneActionTwo = new PlanDetail();
        dayOneActionTwo.setId(102L);
        dayOneActionTwo.setDayNumber(1);
        dayOneActionTwo.setOrderInDay(2);
        dayOneActionTwo.setVideo(plank);

        PlanDetail dayTwoActionOne = new PlanDetail();
        dayTwoActionOne.setId(103L);
        dayTwoActionOne.setDayNumber(2);
        dayTwoActionOne.setOrderInDay(1);
        dayTwoActionOne.setVideo(plank);

        when(workoutPlanRepository.findById(1L)).thenReturn(Optional.of(workoutPlan));
        when(planDetailRepository.findAllByWorkoutPlan_IdOrderByDayNumberAscOrderInDayAsc(1L))
                .thenReturn(List.of(dayOneActionOne, dayOneActionTwo, dayTwoActionOne));

        WorkoutPlanDetailDto result = planDetailService.getWorkoutPlanDetail(1L);

        assertNotNull(result);
        assertEquals(1L, result.getPlanId());
        assertEquals("个性减脂计划", result.getPlanName());
        assertEquals(2, result.getTotalCourseCount());
        assertEquals(2, result.getCourseList().size());
        assertEquals("第1天训练", result.getCourseList().get(0).getCourseName());
        assertEquals(2, result.getCourseList().get(0).getActionList().size());
        assertEquals("深蹲", result.getCourseList().get(0).getActionList().get(0).getActionName());
        assertEquals("平板支撑", result.getCourseList().get(0).getActionList().get(1).getActionName());
    }

    @Test
    void getTodayWorkoutPlanCoursesUsesLatestActivePlanForCurrentDate() {
        User user = new User();
        user.setId(8L);

        WorkoutPlan oldPlan = new WorkoutPlan();
        oldPlan.setId(1L);
        oldPlan.setName("旧计划");
        oldPlan.setStartDate(LocalDate.of(2026, 4, 1));
        oldPlan.setEndDate(LocalDate.of(2026, 4, 28));

        WorkoutPlan newPlan = new WorkoutPlan();
        newPlan.setId(2L);
        newPlan.setName("新计划");
        newPlan.setStartDate(LocalDate.of(2026, 4, 12));
        newPlan.setEndDate(LocalDate.of(2026, 5, 9));

        Video video = new Video();
        video.setId(11L);
        video.setTitle("深蹲");
        video.setUrl("https://example.com/squat.mp4");
        video.setDuration(120);
        video.setCoverUrl("https://example.com/squat.jpg");

        PlanDetail planDetail = new PlanDetail();
        planDetail.setId(101L);
        planDetail.setDayNumber(1);
        planDetail.setOrderInDay(1);
        planDetail.setVideo(video);

        when(userService.getCurrentLoginUserDetails()).thenReturn(user);
        when(workoutPlanRepository.findAll(org.mockito.ArgumentMatchers.<org.springframework.data.jpa.domain.Specification<WorkoutPlan>>any()))
                .thenReturn(List.of(oldPlan, newPlan));
        when(planDetailRepository.findAllByWorkoutPlan_IdAndDayNumberOrderByOrderInDayAsc(2L, 1))
                .thenReturn(List.of(planDetail));

        List<org.example.api.dto.WorkoutPlanCourseDto> result =
                planDetailService.getTodayWorkoutPlanCourses(LocalDate.of(2026, 4, 12));

        assertEquals(1, result.size());
        assertEquals("新计划", result.get(0).getPlanName());
        assertEquals("第1天训练", result.get(0).getCourseName());
        assertEquals(1, result.get(0).getActionList().size());
    }
}
