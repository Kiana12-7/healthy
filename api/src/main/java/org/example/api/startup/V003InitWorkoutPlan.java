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
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Component
public class V003InitWorkoutPlan implements CommandLineRunner, Ordered {

    public static final int order = 102;
    private static final String SYSTEM_TEMPLATE_USERNAME = "system_template_user";
    private static final String SYSTEM_TEMPLATE_NAME = "系统模板用户";
    private static final String SYSTEM_TEMPLATE_FORM_DESCRIPTION = "系统预设训练计划模板";
    private static final long DEFAULT_PLAN_DURATION_DAYS = 30L;

    private final WorkoutPlanService workoutPlanService;
    private final PlanDetailService planDetailService;
    private final WorkoutPlanRepository workoutPlanRepository;
    private final PlanDetailRepository planDetailRepository;
    private final UserRepository userRepository;
    private final FitnessFormRepository fitnessFormRepository;

    public V003InitWorkoutPlan(WorkoutPlanService workoutPlanService,
                               PlanDetailService planDetailService,
                               WorkoutPlanRepository workoutPlanRepository,
                               PlanDetailRepository planDetailRepository,
                               UserRepository userRepository,
                               FitnessFormRepository fitnessFormRepository) {
        this.workoutPlanService = workoutPlanService;
        this.planDetailService = planDetailService;
        this.workoutPlanRepository = workoutPlanRepository;
        this.planDetailRepository = planDetailRepository;
        this.userRepository = userRepository;
        this.fitnessFormRepository = fitnessFormRepository;
    }

    @Override
    @Transactional
    public void run(String... args) {
        User systemUser = ensureSystemUser();
        FitnessForm systemForm = ensureSystemFitnessForm(systemUser);

        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(DEFAULT_PLAN_DURATION_DAYS);

        int createdCount = 0;
        int detailsInitializedCount = 0;
        for (String name : getPlanNames()) {
            WorkoutPlan workoutPlan = workoutPlanRepository
                    .findFirstByNameAndFitnessForm_IdOrderByIdAsc(name, systemForm.getId())
                    .orElse(null);

            if (workoutPlan == null) {
                workoutPlan = workoutPlanService.save(startDate, endDate, systemForm.getId(), name);
                createdCount++;
            }

            if (workoutPlan.getStartDate() == null || workoutPlan.getEndDate() == null) {
                workoutPlan.setStartDate(startDate);
                workoutPlan.setEndDate(endDate);
                workoutPlan = workoutPlanRepository.save(workoutPlan);
            }

            detailsInitializedCount += rebuildPlanDetailsIfNeeded(workoutPlan);
        }

        if (createdCount == 0 && detailsInitializedCount == 0) {
            System.out.println("预设计划及详情已存在，跳过初始化");
            return;
        }

        System.out.println("成功初始化 " + createdCount + " 个预设计划，补齐 " + detailsInitializedCount + " 条计划详情");
    }

    /**
     * 确保存在一个系统模板用户，用于承载预设计划
     */
    private User ensureSystemUser() {
        return userRepository.findByUsername(SYSTEM_TEMPLATE_USERNAME).orElseGet(() -> {
            User user = new User();
            user.setUsername(SYSTEM_TEMPLATE_USERNAME);
            user.setName(SYSTEM_TEMPLATE_NAME);
            return userRepository.save(user);
        });
    }

    /**
     * 确保系统模板用户存在对应表单，用于绑定预设计划
     */
    private FitnessForm ensureSystemFitnessForm(User systemUser) {
        return fitnessFormRepository.findByUser(systemUser).orElseGet(() -> {
            FitnessForm form = new FitnessForm();
            form.setUser(systemUser);
            form.setDescription(SYSTEM_TEMPLATE_FORM_DESCRIPTION);
            return fitnessFormRepository.save(form);
        });
    }

    private int rebuildPlanDetailsIfNeeded(WorkoutPlan workoutPlan) {
        List<List<String>> monthlyPlan = buildMonthlyPlanDetails(workoutPlan.getName());
        long existingCount = planDetailRepository.countByWorkoutPlan_Id(workoutPlan.getId());
        long expectedCount = monthlyPlan.stream().mapToLong(List::size).sum();

        if (existingCount == expectedCount) {
            return 0;
        }

        if (existingCount > 0) {
            planDetailRepository.deleteByWorkoutPlan_Id(workoutPlan.getId());
        }

        int savedCount = 0;
        for (int dayIndex = 0; dayIndex < monthlyPlan.size(); dayIndex++) {
            List<String> actions = monthlyPlan.get(dayIndex);
            for (int orderIndex = 0; orderIndex < actions.size(); orderIndex++) {
                planDetailService.save(dayIndex + 1, orderIndex + 1, workoutPlan.getId(), actions.get(orderIndex));
                savedCount++;
            }
        }

        return savedCount;
    }

    private List<List<String>> buildMonthlyPlanDetails(String planName) {
        List<List<String>> cycleTemplate = getCycleTemplate(resolvePlanType(planName));
        List<List<String>> monthlyPlan = new ArrayList<>();

        for (int day = 0; day < DEFAULT_PLAN_DURATION_DAYS; day++) {
            monthlyPlan.add(cycleTemplate.get(day % cycleTemplate.size()));
        }

        return monthlyPlan;
    }

    private PlanType resolvePlanType(String planName) {
        if (containsAny(planName, "肩颈", "疼痛", "恢复", "睡眠", "瑜伽")) {
            return PlanType.RECOVERY;
        }
        if (containsAny(planName, "跑步", "5公里", "马拉松", "漫跑", "单车")) {
            return PlanType.ENDURANCE;
        }
        if (containsAny(planName, "跳绳", "搏击有氧")) {
            return PlanType.CARDIO;
        }
        if (containsAny(planName, "胸肩", "肩臂", "上肢", "脂肪胸")) {
            return PlanType.UPPER_BODY;
        }
        if (containsAny(planName, "腹", "肚腩", "马甲线")) {
            return PlanType.CORE;
        }
        if (containsAny(planName, "增肌")) {
            return PlanType.MUSCLE_GAIN;
        }
        if (containsAny(planName, "大体重")) {
            return PlanType.LOW_IMPACT_FAT_LOSS;
        }
        if (containsAny(planName, "体质增强")) {
            return PlanType.FOUNDATION;
        }
        if (containsAny(planName, "减脂", "燃脂", "瘦", "减围")) {
            return PlanType.FAT_LOSS;
        }
        return PlanType.FOUNDATION;
    }

    private List<List<String>> getCycleTemplate(PlanType planType) {
        return switch (planType) {
            case FAT_LOSS -> List.of(
                    List.of("深蹲", "俯卧撑", "平板支撑"),
                    List.of("弓步蹲", "划船", "卷腹"),
                    List.of("硬拉", "深蹲", "平板支撑"),
                    List.of("俯卧撑", "弓步蹲", "卷腹"),
                    List.of("划船", "深蹲", "平板支撑")
            );
            case LOW_IMPACT_FAT_LOSS -> List.of(
                    List.of("深蹲", "划船", "平板支撑"),
                    List.of("弓步蹲", "卷腹", "平板支撑"),
                    List.of("深蹲", "肩推", "划船"),
                    List.of("卷腹", "平板支撑", "弓步蹲"),
                    List.of("划船", "深蹲", "平板支撑")
            );
            case MUSCLE_GAIN -> List.of(
                    List.of("卧推", "深蹲", "划船"),
                    List.of("硬拉", "肩推", "二头弯举"),
                    List.of("引体向上", "卧推", "平板支撑"),
                    List.of("深蹲", "划船", "俯卧撑"),
                    List.of("硬拉", "肩推", "引体向上")
            );
            case UPPER_BODY -> List.of(
                    List.of("卧推", "划船", "肩推"),
                    List.of("俯卧撑", "引体向上", "二头弯举"),
                    List.of("卧推", "肩推", "平板支撑"),
                    List.of("划船", "二头弯举", "俯卧撑"),
                    List.of("引体向上", "卧推", "划船")
            );
            case CORE -> List.of(
                    List.of("卷腹", "平板支撑", "俯卧撑"),
                    List.of("深蹲", "卷腹", "平板支撑"),
                    List.of("弓步蹲", "平板支撑", "划船"),
                    List.of("卷腹", "俯卧撑", "平板支撑"),
                    List.of("深蹲", "弓步蹲", "卷腹")
            );
            case ENDURANCE -> List.of(
                    List.of("深蹲", "弓步蹲", "平板支撑"),
                    List.of("硬拉", "卷腹", "划船"),
                    List.of("深蹲", "平板支撑", "卷腹"),
                    List.of("弓步蹲", "硬拉", "平板支撑"),
                    List.of("深蹲", "卷腹", "划船")
            );
            case CARDIO -> List.of(
                    List.of("深蹲", "弓步蹲", "平板支撑"),
                    List.of("俯卧撑", "卷腹", "深蹲"),
                    List.of("弓步蹲", "平板支撑", "卷腹"),
                    List.of("深蹲", "俯卧撑", "划船"),
                    List.of("弓步蹲", "卷腹", "平板支撑")
            );
            case RECOVERY -> List.of(
                    List.of("平板支撑", "划船", "肩推"),
                    List.of("卷腹", "弓步蹲", "平板支撑"),
                    List.of("划船", "肩推", "卷腹"),
                    List.of("平板支撑", "深蹲", "划船"),
                    List.of("肩推", "弓步蹲", "平板支撑")
            );
            case FOUNDATION -> List.of(
                    List.of("深蹲", "俯卧撑", "划船"),
                    List.of("弓步蹲", "平板支撑", "卷腹"),
                    List.of("卧推", "深蹲", "肩推"),
                    List.of("划船", "卷腹", "平板支撑"),
                    List.of("硬拉", "俯卧撑", "弓步蹲")
            );
        };
    }

    private boolean containsAny(String text, String... keywords) {
        return Arrays.stream(keywords).anyMatch(text::contains);
    }

    /**
     * 从你提供的 38 个计划中提取名称列表
     */
    private List<String> getPlanNames() {
        return Arrays.asList(
                "个性减脂计划",
                "告别肚腩计划",
                "学生党·全身增肌计划",
                "大正爱跑步·轻松拿捏5公里…",
                "个性跑步计划",
                "定制大体重计划",
                "瘦腹减围·型男打造计划",
                "10天冲刺·极速燃脂计划",
                "高质量睡眠计划",
                "轻松燃脂·个性跑步计划",
                "告别脂肪胸计划",
                "全身突击燃脂计划",
                "全身增肌·型男打造计划",
                "大正爱跑步·全力奔跑5公里",
                "腹肌撕裂计划",
                "热汗瑜伽·减脂塑形计划",
                "单车智能计划",
                "跳绳·高效燃脂计划",
                "告别疼痛·肩颈改善计划",
                "肩臂·强化增肌计划",
                "7天冲刺·全身燃脂计划",
                "定制瘦身计划",
                "经典胸肩·强效增肌计划",
                "7天瘦全身·晚安燃脂计划",
                "学生专属·瘦全身计划",
                "肩臂减脂计划",
                "科林滚滚·搏击有氧计划",
                "学生升学生涯减脂计划",
                "哑铃上肢增肌计划",
                "马甲线控制·会员塑造计划",
                "学生燃脂计划",
                "优质增肌雕刻计划",
                "21天健康体质增强计划",
                "7天体能恢复计划",
                "快燃元气·宫吊漫跑计划",
                "跳绳燃脂计划",
                "高效燃脂·保持健康计划",
                "备战体测·高原模拟马拉松备赛"
        );
    }

    @Override
    public int getOrder() {
        return order;
    }

    private enum PlanType {
        FAT_LOSS,
        LOW_IMPACT_FAT_LOSS,
        MUSCLE_GAIN,
        UPPER_BODY,
        CORE,
        ENDURANCE,
        CARDIO,
        RECOVERY,
        FOUNDATION
    }
}
