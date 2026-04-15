package org.example.api.service;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.example.api.dto.CurrentWorkoutPlanSummaryDTO;
import org.example.api.dto.WorkoutPlanListDto;
import org.example.api.entity.FitnessForm;
import org.example.api.entity.User;
import org.example.api.entity.WorkoutPlan;
import org.example.api.repository.FitnessFormRepository;
import org.example.api.repository.WorkoutPlanRepository;
import org.example.api.repository.specs.WorkoutPlanSpec;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

@Service
public class WorkoutPlanServiceImpl implements WorkoutPlanService{
    private static final String SYSTEM_TEMPLATE_USERNAME = "system_template_user";

    private final WorkoutPlanRepository workoutPlanRepository;
    private final UserService userService;
    private final FitnessFormRepository fitnessFormRepository;
    private final AIPlanGenerator aiPlanGenerator;

    public WorkoutPlanServiceImpl(WorkoutPlanRepository workoutPlanRepository,
                                  UserService userService,
                                  FitnessFormRepository fitnessFormRepository,
                                  AIPlanGenerator aiPlanGenerator) {
        this.workoutPlanRepository = workoutPlanRepository;
        this.userService = userService;
        this.fitnessFormRepository = fitnessFormRepository;
        this.aiPlanGenerator = aiPlanGenerator;
    }

    @Override
    public WorkoutPlan generatePlan() {
        // 获取当前用户id
        User currentUser = this.userService.getCurrentUser().orElseThrow(EntityExistsException::new);
        // 通过当前用户id获取器健康表单
        FitnessForm form = this.fitnessFormRepository.findByUser(currentUser).orElseThrow(EntityNotFoundException::new);
        // 调用生成器中的生成方法
        return this.aiPlanGenerator.generatePlan(form);
    }

    @Override
    public WorkoutPlan save(LocalDate startTime, LocalDate endTime, Long fitnessFormId) {
        return this.save(startTime, endTime,fitnessFormId, "ai生成计划");
    }

    @Override
    public WorkoutPlan save(LocalDate startTime, LocalDate endTime, Long fitnessFormId, String name) {
        // 保存WorkoutPlan实体
        WorkoutPlan workoutPlan = new WorkoutPlan();
        workoutPlan.setStartDate(startTime);
        workoutPlan.setEndDate(endTime);
        FitnessForm fitnessForm = this.fitnessFormRepository.findById(fitnessFormId).orElseThrow(EntityExistsException::new);
        workoutPlan.setFitnessForm(fitnessForm);
        workoutPlan.setName(name);
        workoutPlan.setUser(fitnessForm.getUser());

        return this.workoutPlanRepository.save(workoutPlan);
    }

    @Override
    public List<WorkoutPlanListDto> getTemplatePlanList(String keyword) {
        String normalizedKeyword = keyword == null ? "" : keyword.trim();

        return this.workoutPlanRepository.findAllByUser_UsernameOrderByIdAsc(SYSTEM_TEMPLATE_USERNAME)
                .stream()
                .filter(workoutPlan -> normalizedKeyword.isEmpty() || containsKeyword(workoutPlan.getName(), normalizedKeyword))
                .map(this::toWorkoutPlanListDto)
                .toList();
    }

    @Override
    public CurrentWorkoutPlanSummaryDTO getCurrentPlanSummary(LocalDate date) {
        User currentUser = this.userService.getCurrentLoginUserDetails();
        Specification<WorkoutPlan> spec = WorkoutPlanSpec.isDate(date).and(WorkoutPlanSpec.isUser(currentUser));
        WorkoutPlan latestPlan = this.workoutPlanRepository.findAll(spec).stream()
                .filter(workoutPlan -> workoutPlan.getStartDate() != null && workoutPlan.getEndDate() != null)
                .max(
                        Comparator.comparing(WorkoutPlan::getStartDate)
                                .thenComparing(WorkoutPlan::getId, Comparator.nullsLast(Comparator.naturalOrder()))
                )
                .orElse(null);

        CurrentWorkoutPlanSummaryDTO dto = new CurrentWorkoutPlanSummaryDTO();
        if (latestPlan == null) {
            dto.setHasActivePlan(false);
            return dto;
        }

        dto.setHasActivePlan(true);
        dto.setPlanId(latestPlan.getId());
        dto.setPlanName(latestPlan.getName());
        dto.setStartDate(latestPlan.getStartDate());
        dto.setEndDate(latestPlan.getEndDate());
        dto.setCurrentDay((int) ChronoUnit.DAYS.between(latestPlan.getStartDate(), date) + 1);
        dto.setTotalDays((int) ChronoUnit.DAYS.between(latestPlan.getStartDate(), latestPlan.getEndDate()) + 1);
        return dto;
    }

    private boolean containsKeyword(String planName, String keyword) {
        if (planName == null || keyword.isBlank()) {
            return false;
        }
        return planName.toLowerCase(Locale.ROOT).contains(keyword.toLowerCase(Locale.ROOT));
    }

    private WorkoutPlanListDto toWorkoutPlanListDto(WorkoutPlan workoutPlan) {
        WorkoutPlanListDto dto = new WorkoutPlanListDto();
        dto.setId(workoutPlan.getId());
        dto.setName(workoutPlan.getName());
        dto.setStartDate(workoutPlan.getStartDate());
        dto.setEndDate(workoutPlan.getEndDate());
        return dto;
    }

}
