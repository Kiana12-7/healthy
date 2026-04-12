package org.example.api.service;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.example.api.entity.FitnessForm;
import org.example.api.entity.User;
import org.example.api.entity.WorkoutPlan;
import org.example.api.repository.FitnessFormRepository;
import org.example.api.repository.WorkoutPlanRepository;
import org.example.api.repository.specs.FitnessFormSpec;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class WorkoutPlanServiceImpl implements WorkoutPlanService{
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
        FitnessForm form = this.fitnessFormRepository.findBy(FitnessFormSpec.isUser(currentUser)).orElseThrow(EntityNotFoundException::new);
        // 调用生成器中的生成方法
        return this.aiPlanGenerator.generatePlan(form);
    }

    @Override
    public WorkoutPlan save(LocalDate startTime, LocalDate endTime, Long fitnessFormId) {
        // 保存WorkoutPlan实体
        WorkoutPlan workoutPlan = new WorkoutPlan();
        workoutPlan.setStartDate(startTime);
        workoutPlan.setEndDate(endTime);
        FitnessForm fitnessForm = this.fitnessFormRepository.findById(fitnessFormId).orElseThrow(EntityExistsException::new);
        workoutPlan.setFitnessForm(fitnessForm);
        workoutPlan.setUser(fitnessForm.getUser());

        return this.workoutPlanRepository.save(workoutPlan);
    }
}
