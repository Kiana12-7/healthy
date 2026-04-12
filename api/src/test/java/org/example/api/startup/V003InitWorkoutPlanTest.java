package org.example.api.startup;

import org.example.api.entity.FitnessForm;
import org.example.api.entity.User;
import org.example.api.entity.WorkoutPlan;
import org.example.api.repository.FitnessFormRepository;
import org.example.api.repository.PlanDetailRepository;
import org.example.api.repository.UserRepository;
import org.example.api.repository.WorkoutPlanRepository;
import org.example.api.service.PlanDetailService;
import org.example.api.service.WorkoutPlanService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class V003InitWorkoutPlanTest {

    @Mock
    private WorkoutPlanService workoutPlanService;

    @Mock
    private PlanDetailService planDetailService;

    @Mock
    private WorkoutPlanRepository workoutPlanRepository;

    @Mock
    private PlanDetailRepository planDetailRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private FitnessFormRepository fitnessFormRepository;

    private V003InitWorkoutPlan initWorkoutPlan;

    @BeforeEach
    void setUp() {
        initWorkoutPlan = new V003InitWorkoutPlan(
                workoutPlanService,
                planDetailService,
                workoutPlanRepository,
                planDetailRepository,
                userRepository,
                fitnessFormRepository
        );
    }

    @Test
    void runCreatesMissingSystemRecordsAndPlans() {
        User systemUser = new User();
        systemUser.setId(10L);
        systemUser.setUsername("system_template_user");

        FitnessForm systemForm = new FitnessForm();
        systemForm.setId(20L);
        systemForm.setUser(systemUser);

        WorkoutPlan workoutPlan = new WorkoutPlan();
        workoutPlan.setId(30L);
        workoutPlan.setName("个性减脂计划");
        workoutPlan.setStartDate(LocalDate.now());
        workoutPlan.setEndDate(LocalDate.now().plusDays(30));

        when(userRepository.findByUsername("system_template_user")).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(systemUser);
        when(fitnessFormRepository.findByUser(systemUser)).thenReturn(Optional.empty());
        when(fitnessFormRepository.save(any(FitnessForm.class))).thenReturn(systemForm);
        when(workoutPlanRepository.findFirstByNameAndFitnessForm_IdOrderByIdAsc(anyString(), eq(20L))).thenReturn(Optional.empty());
        when(workoutPlanService.save(any(LocalDate.class), any(LocalDate.class), eq(20L), anyString())).thenReturn(workoutPlan);
        when(planDetailRepository.countByWorkoutPlan_Id(30L)).thenReturn(0L);

        initWorkoutPlan.run();

        verify(userRepository).save(any(User.class));
        verify(fitnessFormRepository).save(any(FitnessForm.class));
        verify(workoutPlanService, times(38))
                .save(any(LocalDate.class), any(LocalDate.class), eq(20L), anyString());
        verify(planDetailService, times(3420))
                .save(anyInt(), anyInt(), eq(30L), anyString());
    }

    @Test
    void runSkipsPlansThatAlreadyExistForSystemForm() {
        User systemUser = new User();
        systemUser.setId(10L);
        systemUser.setUsername("system_template_user");

        FitnessForm systemForm = new FitnessForm();
        systemForm.setId(20L);
        systemForm.setUser(systemUser);

        WorkoutPlan workoutPlan = new WorkoutPlan();
        workoutPlan.setId(30L);
        workoutPlan.setName("个性减脂计划");
        workoutPlan.setStartDate(LocalDate.now());
        workoutPlan.setEndDate(LocalDate.now().plusDays(30));

        when(userRepository.findByUsername("system_template_user")).thenReturn(Optional.of(systemUser));
        when(fitnessFormRepository.findByUser(systemUser)).thenReturn(Optional.of(systemForm));
        when(workoutPlanRepository.findFirstByNameAndFitnessForm_IdOrderByIdAsc(anyString(), eq(20L))).thenReturn(Optional.of(workoutPlan));
        when(planDetailRepository.countByWorkoutPlan_Id(30L)).thenReturn(90L);

        initWorkoutPlan.run();

        verify(userRepository, never()).save(any(User.class));
        verify(fitnessFormRepository, never()).save(any(FitnessForm.class));
        verify(workoutPlanService, never())
                .save(any(LocalDate.class), any(LocalDate.class), eq(20L), anyString());
        verify(planDetailRepository, never()).deleteByWorkoutPlan_Id(30L);
        verify(planDetailService, never()).save(anyInt(), anyInt(), eq(30L), anyString());
    }

    @Test
    void runBackfillsPlanDetailsForExistingPlanWithoutDetails() {
        User systemUser = new User();
        systemUser.setId(10L);
        systemUser.setUsername("system_template_user");

        FitnessForm systemForm = new FitnessForm();
        systemForm.setId(20L);
        systemForm.setUser(systemUser);

        WorkoutPlan workoutPlan = new WorkoutPlan();
        workoutPlan.setId(30L);
        workoutPlan.setName("个性减脂计划");
        workoutPlan.setStartDate(LocalDate.now());
        workoutPlan.setEndDate(LocalDate.now().plusDays(30));

        when(userRepository.findByUsername("system_template_user")).thenReturn(Optional.of(systemUser));
        when(fitnessFormRepository.findByUser(systemUser)).thenReturn(Optional.of(systemForm));
        when(workoutPlanRepository.findFirstByNameAndFitnessForm_IdOrderByIdAsc(anyString(), eq(20L))).thenReturn(Optional.of(workoutPlan));
        when(planDetailRepository.countByWorkoutPlan_Id(30L)).thenReturn(0L);

        initWorkoutPlan.run();

        verify(workoutPlanService, never())
                .save(any(LocalDate.class), any(LocalDate.class), eq(20L), anyString());
        verify(planDetailService, times(3420))
                .save(anyInt(), anyInt(), eq(30L), anyString());
    }
}
