package org.example.api.service;

import org.example.api.dto.WorkoutDurationRecordRequestDTO;
import org.example.api.dto.WorkoutDurationSummaryDTO;
import org.example.api.entity.User;
import org.example.api.entity.WorkoutDurationStat;
import org.example.api.enums.WorkoutDurationSourceType;
import org.example.api.repository.WorkoutDurationStatRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WorkoutDurationServiceImplTest {

    @Mock
    private WorkoutDurationStatRepository workoutDurationStatRepository;

    @Mock
    private UserService userService;

    private WorkoutDurationServiceImpl workoutDurationService;

    @BeforeEach
    void setUp() {
        workoutDurationService = new WorkoutDurationServiceImpl(workoutDurationStatRepository, userService);
    }

    @Test
    void recordDurationAddsSecondsToAiPlanBucket() {
        User user = new User();
        user.setId(1L);

        WorkoutDurationStat stat = new WorkoutDurationStat();
        stat.setId(10L);
        stat.setUser(user);
        stat.setRecordDate(LocalDate.of(2026, 4, 13));
        stat.setPlanDurationSeconds(180);
        stat.setAiPlanDurationSeconds(120);
        stat.setTotalDurationSeconds(300);

        WorkoutDurationRecordRequestDTO requestDTO = new WorkoutDurationRecordRequestDTO();
        requestDTO.setRecordDate(LocalDate.of(2026, 4, 13));
        requestDTO.setDurationSeconds(90);
        requestDTO.setSourceType(WorkoutDurationSourceType.AI_PLAN);

        when(userService.getCurrentLoginUserDetails()).thenReturn(user);
        when(workoutDurationStatRepository.findByUserAndRecordDate(user, LocalDate.of(2026, 4, 13)))
                .thenReturn(Optional.of(stat));

        workoutDurationService.recordDuration(requestDTO);

        ArgumentCaptor<WorkoutDurationStat> statCaptor = ArgumentCaptor.forClass(WorkoutDurationStat.class);
        verify(workoutDurationStatRepository).save(statCaptor.capture());

        assertEquals(180, statCaptor.getValue().getPlanDurationSeconds());
        assertEquals(210, statCaptor.getValue().getAiPlanDurationSeconds());
        assertEquals(390, statCaptor.getValue().getTotalDurationSeconds());
    }

    @Test
    void getSummaryFillsMissingDatesAndAggregatesTotals() {
        User user = new User();
        user.setId(1L);

        WorkoutDurationStat firstDay = new WorkoutDurationStat();
        firstDay.setRecordDate(LocalDate.of(2026, 4, 11));
        firstDay.setPlanDurationSeconds(600);
        firstDay.setAiPlanDurationSeconds(0);
        firstDay.setTotalDurationSeconds(600);

        WorkoutDurationStat thirdDay = new WorkoutDurationStat();
        thirdDay.setRecordDate(LocalDate.of(2026, 4, 13));
        thirdDay.setPlanDurationSeconds(300);
        thirdDay.setAiPlanDurationSeconds(900);
        thirdDay.setTotalDurationSeconds(1200);

        when(userService.getCurrentLoginUserDetails()).thenReturn(user);
        when(workoutDurationStatRepository.findAllByUserAndRecordDateBetweenOrderByRecordDateAsc(
                user,
                LocalDate.of(2026, 4, 11),
                LocalDate.of(2026, 4, 13)
        )).thenReturn(List.of(firstDay, thirdDay));

        WorkoutDurationSummaryDTO summary = workoutDurationService.getSummary(
                LocalDate.of(2026, 4, 11),
                LocalDate.of(2026, 4, 13)
        );

        assertEquals(1800, summary.getTotalDurationSeconds());
        assertEquals(900, summary.getPlanDurationSeconds());
        assertEquals(900, summary.getAiPlanDurationSeconds());
        assertEquals(2, summary.getActiveDays());
        assertEquals(3, summary.getDailyRecords().size());
        assertEquals(0, summary.getDailyRecords().get(1).getTotalDurationSeconds());
        assertEquals(1200, summary.getDailyRecords().get(2).getTotalDurationSeconds());
    }
}
