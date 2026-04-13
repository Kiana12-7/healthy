package org.example.api.service;

import org.example.api.dto.WorkoutDurationDailyDTO;
import org.example.api.dto.WorkoutDurationRecordRequestDTO;
import org.example.api.dto.WorkoutDurationSummaryDTO;
import org.example.api.entity.User;
import org.example.api.entity.WorkoutDurationStat;
import org.example.api.enums.WorkoutDurationSourceType;
import org.example.api.repository.WorkoutDurationStatRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class WorkoutDurationServiceImpl implements WorkoutDurationService {
    private final WorkoutDurationStatRepository workoutDurationStatRepository;
    private final UserService userService;

    public WorkoutDurationServiceImpl(WorkoutDurationStatRepository workoutDurationStatRepository,
                                      UserService userService) {
        this.workoutDurationStatRepository = workoutDurationStatRepository;
        this.userService = userService;
    }

    @Override
    public void recordDuration(WorkoutDurationRecordRequestDTO requestDTO) {
        if (requestDTO.getRecordDate() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "recordDate 不能为空");
        }
        if (requestDTO.getSourceType() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "sourceType 不能为空");
        }
        if (requestDTO.getDurationSeconds() == null || requestDTO.getDurationSeconds() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "durationSeconds 必须大于 0");
        }

        User currentUser = userService.getCurrentLoginUserDetails();
        WorkoutDurationStat stat = workoutDurationStatRepository.findByUserAndRecordDate(currentUser, requestDTO.getRecordDate())
                .orElseGet(() -> createNewStat(currentUser, requestDTO.getRecordDate()));

        if (requestDTO.getSourceType() == WorkoutDurationSourceType.AI_PLAN) {
            stat.setAiPlanDurationSeconds(stat.getAiPlanDurationSeconds() + requestDTO.getDurationSeconds());
        } else {
            stat.setPlanDurationSeconds(stat.getPlanDurationSeconds() + requestDTO.getDurationSeconds());
        }
        stat.setTotalDurationSeconds(stat.getPlanDurationSeconds() + stat.getAiPlanDurationSeconds());

        workoutDurationStatRepository.save(stat);
    }

    @Override
    public WorkoutDurationSummaryDTO getSummary(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "开始日期和结束日期不能为空");
        }
        if (startDate.isAfter(endDate)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "开始日期不能晚于结束日期");
        }

        User currentUser = userService.getCurrentLoginUserDetails();
        List<WorkoutDurationStat> stats = workoutDurationStatRepository.findAllByUserAndRecordDateBetweenOrderByRecordDateAsc(currentUser, startDate, endDate);
        Map<LocalDate, WorkoutDurationStat> statsByDate = stats.stream()
                .collect(Collectors.toMap(WorkoutDurationStat::getRecordDate, Function.identity()));

        List<WorkoutDurationDailyDTO> dailyRecords = new ArrayList<>();
        int totalDurationSeconds = 0;
        int planDurationSeconds = 0;
        int aiPlanDurationSeconds = 0;
        int activeDays = 0;

        for (LocalDate cursor = startDate; !cursor.isAfter(endDate); cursor = cursor.plusDays(1)) {
            WorkoutDurationStat stat = statsByDate.get(cursor);
            int currentPlanSeconds = stat != null ? stat.getPlanDurationSeconds() : 0;
            int currentAiSeconds = stat != null ? stat.getAiPlanDurationSeconds() : 0;
            int currentTotalSeconds = currentPlanSeconds + currentAiSeconds;

            if (currentTotalSeconds > 0) {
                activeDays++;
            }

            planDurationSeconds += currentPlanSeconds;
            aiPlanDurationSeconds += currentAiSeconds;
            totalDurationSeconds += currentTotalSeconds;

            dailyRecords.add(new WorkoutDurationDailyDTO(
                    cursor,
                    currentPlanSeconds,
                    currentAiSeconds,
                    currentTotalSeconds
            ));
        }

        return new WorkoutDurationSummaryDTO(
                totalDurationSeconds,
                planDurationSeconds,
                aiPlanDurationSeconds,
                activeDays,
                dailyRecords
        );
    }

    private WorkoutDurationStat createNewStat(User currentUser, LocalDate recordDate) {
        WorkoutDurationStat stat = new WorkoutDurationStat();
        stat.setUser(currentUser);
        stat.setRecordDate(recordDate);
        stat.setPlanDurationSeconds(0);
        stat.setAiPlanDurationSeconds(0);
        stat.setTotalDurationSeconds(0);
        return stat;
    }
}
