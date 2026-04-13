package org.example.api.service;

import org.example.api.dto.CurrentWorkoutPlanSummaryDTO;
import org.example.api.entity.FitnessForm;
import org.example.api.entity.User;
import org.example.api.entity.WorkoutPlan;
import org.example.api.repository.FitnessFormRepository;
import org.example.api.repository.WorkoutPlanRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WorkoutPlanServiceImplTest {

    @Mock
    private WorkoutPlanRepository workoutPlanRepository;

    @Mock
    private UserService userService;

    @Mock
    private FitnessFormRepository fitnessFormRepository;

    @Mock
    private AIPlanGenerator aiPlanGenerator;

    private WorkoutPlanServiceImpl workoutPlanService;

    @BeforeEach
    void setUp() {
        workoutPlanService = new WorkoutPlanServiceImpl(
                workoutPlanRepository,
                userService,
                fitnessFormRepository,
                aiPlanGenerator
        );
    }

    @Test
    void getCurrentPlanSummaryReturnsLatestActivePlan() {
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
        newPlan.setStartDate(LocalDate.of(2026, 4, 10));
        newPlan.setEndDate(LocalDate.of(2026, 5, 7));

        when(userService.getCurrentLoginUserDetails()).thenReturn(user);
        when(workoutPlanRepository.findAll(org.mockito.ArgumentMatchers.<Specification<WorkoutPlan>>any()))
                .thenReturn(List.of(oldPlan, newPlan));

        CurrentWorkoutPlanSummaryDTO summary = workoutPlanService.getCurrentPlanSummary(LocalDate.of(2026, 4, 13));

        assertTrue(summary.isHasActivePlan());
        assertEquals("新计划", summary.getPlanName());
        assertEquals(4, summary.getCurrentDay());
        assertEquals(28, summary.getTotalDays());
    }

    @Test
    void getCurrentPlanSummaryReturnsEmptyWhenNoPlanIsActive() {
        User user = new User();
        user.setId(8L);

        when(userService.getCurrentLoginUserDetails()).thenReturn(user);
        when(workoutPlanRepository.findAll(org.mockito.ArgumentMatchers.<Specification<WorkoutPlan>>any()))
                .thenReturn(List.of());

        CurrentWorkoutPlanSummaryDTO summary = workoutPlanService.getCurrentPlanSummary(LocalDate.of(2026, 4, 13));

        assertFalse(summary.isHasActivePlan());
    }
}
