package org.example.api.startup;

import org.example.api.entity.FitnessForm;
import org.example.api.entity.User;
import org.example.api.entity.WorkoutPlan;
import org.example.api.repository.FitnessFormRepository;
import org.example.api.repository.UserRepository;
import org.example.api.repository.WorkoutPlanRepository;
import org.example.api.service.PlanDetailService;
import org.example.api.service.WorkoutPlanService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Component
public class V003InitWorkoutPlan implements CommandLineRunner, Ordered {

    public static final int order = 102;

    private final WorkoutPlanService workoutPlanService;
    private final PlanDetailService planDetailService;
    private final WorkoutPlanRepository workoutPlanRepository;
    private final UserRepository userRepository;
    private final FitnessFormRepository fitnessFormRepository;

    // 系统用户固定 ID（也可以动态查询）
    private static final Long SYSTEM_USER_ID = -1L;
    private static final Long SYSTEM_FITNESS_FORM_ID = -1L;

    public V003InitWorkoutPlan(WorkoutPlanService workoutPlanService,
                               PlanDetailService planDetailService,
                               WorkoutPlanRepository workoutPlanRepository,
                               UserRepository userRepository,
                               FitnessFormRepository fitnessFormRepository) {
        this.workoutPlanService = workoutPlanService;
        this.planDetailService = planDetailService;
        this.workoutPlanRepository = workoutPlanRepository;
        this.userRepository = userRepository;
        this.fitnessFormRepository = fitnessFormRepository;
    }

    @Override
    @Transactional
    public void run(String... args) {
//        // 1. 防重复：如果“个性减脂计划”已存在，则跳过全部初始化
//        if (workoutPlanRepository.existsByName("个性减脂计划")) {
//            System.out.println("预设计划已存在，跳过初始化");
//            return;
//        }
//
//        // 2. 确保系统用户和系统健康表单存在
//        ensureSystemUserAndForm();
//
//        // 3. 构建所有计划名称列表（共38个）
//        List<String> planNames = getPlanNames();
//
//        LocalDate startDate = LocalDate.now();
//        LocalDate endDate = startDate.plusDays(30);  // 默认30天
//
//        int count = 0;
//        for (String name : planNames) {
//            workoutPlanService.save(startDate, endDate, SYSTEM_FITNESS_FORM_ID, name);
//            count++;
//        }
//        System.out.println("成功插入 " + count + " 个预设计划");
    }

    /**
     * 确保存在一个系统用户和对应的健康表单，用于关联所有预设计划
     */
    private void ensureSystemUserAndForm() {
        // 创建系统用户（如果不存在）
        User systemUser = userRepository.findById(SYSTEM_USER_ID).orElseGet(() -> {
            User user = new User();
            user.setId(SYSTEM_USER_ID);
            user.setUsername("system_template_user");
            // 其他必要字段可设置默认值（如密码等，根据你的 User 实体要求）
            // 如果 User 实体有非空约束字段，请在这里补充
            return userRepository.save(user);
        });

        // 创建系统健康表单（如果不存在）
        FitnessForm systemForm = fitnessFormRepository.findById(SYSTEM_FITNESS_FORM_ID).orElseGet(() -> {
            FitnessForm form = new FitnessForm();
            form.setId(SYSTEM_FITNESS_FORM_ID);
            form.setUser(systemUser);
            // 其他必要字段设置默认值（如身高、体重等，根据你的 FitnessForm 实体要求）
            // 如果 FitnessForm 有非空字段，请在这里补充
            return fitnessFormRepository.save(form);
        });
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
}