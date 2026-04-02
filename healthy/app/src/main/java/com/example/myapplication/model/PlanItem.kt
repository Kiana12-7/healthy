package com.example.myapplication.model

// 计划实体类
data class PlanItem(
    val name: String,
    val tagIds: List<String>
) {
    companion object {
        // 获取所有38个计划（已绑定标签）
        fun getAllPlans(): List<PlanItem> {
            return listOf(
                PlanItem("个性减脂计划", listOf("goal_fat_loss", "part_whole", "diff_beginner", "crowd_general")),
                PlanItem("告别肚腩计划", listOf("goal_fat_loss", "part_abdominal", "diff_beginner", "crowd_office_worker")),
                PlanItem("学生党·全身增肌计划", listOf("goal_muscle_gain", "part_whole", "diff_beginner", "crowd_student")),
                PlanItem("大正爱跑步·轻松拿捏5公里…", listOf("goal_cardio", "part_leg", "diff_beginner", "crowd_general")),
                PlanItem("个性跑步计划", listOf("goal_cardio", "part_leg", "diff_all", "crowd_general")),
                PlanItem("定制大体重计划", listOf("goal_fat_loss", "part_whole", "diff_beginner", "crowd_heavy")),
                PlanItem("瘦腹减围·型男打造计划", listOf("goal_fat_loss", "part_abdominal", "diff_elementary", "crowd_general")),
                PlanItem("10天冲刺·极速燃脂计划", listOf("goal_fat_loss", "part_whole", "diff_elementary", "crowd_general")),
                PlanItem("高质量睡眠计划", listOf("goal_relax", "part_whole", "diff_beginner", "crowd_general")),
                PlanItem("轻松燃脂·个性跑步计划", listOf("goal_fat_loss", "part_leg", "diff_beginner", "crowd_general")),
                PlanItem("告别脂肪胸计划", listOf("goal_fat_loss", "part_chest", "diff_elementary", "crowd_male")),
                PlanItem("全身突击燃脂计划", listOf("goal_fat_loss", "part_whole", "diff_intermediate", "crowd_general")),
                PlanItem("全身增肌·型男打造计划", listOf("goal_muscle_gain", "part_whole", "diff_elementary", "crowd_general")),
                PlanItem("大正爱跑步·全力奔跑5公里", listOf("goal_cardio", "part_leg", "diff_elementary", "crowd_general")),
                PlanItem("腹肌撕裂计划", listOf("goal_muscle_gain", "part_abdominal", "diff_intermediate", "crowd_general")),
                PlanItem("热汗瑜伽·减脂塑形计划", listOf("goal_fat_loss", "part_whole", "diff_beginner", "crowd_female")),
                PlanItem("单车智能计划", listOf("goal_cardio", "part_leg", "diff_elementary", "crowd_general")),
                PlanItem("跳绳·高效燃脂计划", listOf("goal_fat_loss", "part_whole", "diff_beginner", "crowd_general")),
                PlanItem("告别疼痛·肩颈改善计划", listOf("goal_posture", "part_shoulder", "diff_beginner", "crowd_office_worker")),
                PlanItem("肩臂·强化增肌计划", listOf("goal_muscle_gain", "part_arm", "diff_intermediate", "crowd_general")),
                PlanItem("7天冲刺·全身燃脂计划", listOf("goal_fat_loss", "part_whole", "diff_elementary", "crowd_general")),
                PlanItem("定制瘦身计划", listOf("goal_fat_loss", "part_whole", "diff_beginner", "crowd_general")),
                PlanItem("经典胸肩·强效增肌计划", listOf("goal_muscle_gain", "part_chest", "diff_intermediate", "crowd_general")),
                PlanItem("7天瘦全身·晚安燃脂计划", listOf("goal_fat_loss", "part_whole", "diff_beginner", "crowd_office_worker")),
                PlanItem("学生专属·瘦全身计划", listOf("goal_fat_loss", "part_whole", "diff_beginner", "crowd_student")),
                PlanItem("肩臂减脂计划", listOf("goal_fat_loss", "part_arm", "diff_beginner", "crowd_general")),
                PlanItem("科林滚滚·搏击有氧计划", listOf("goal_fat_loss", "part_whole", "diff_elementary", "crowd_general")),
                PlanItem("学生升学生涯减脂计划", listOf("goal_fat_loss", "part_whole", "diff_beginner", "crowd_student")),
                PlanItem("哑铃上肢增肌计划", listOf("goal_muscle_gain", "part_arm", "diff_elementary", "crowd_general")),
                PlanItem("马甲线控制·会员塑造计划", listOf("goal_fat_loss", "part_abdominal", "diff_intermediate", "crowd_general")),
                PlanItem("学生燃脂计划", listOf("goal_fat_loss", "part_whole", "diff_beginner", "crowd_student")),
                PlanItem("优质增肌雕刻计划", listOf("goal_muscle_gain", "part_whole", "diff_advanced", "crowd_general")),
                PlanItem("21天健康体质增强计划", listOf("goal_health", "part_whole", "diff_beginner", "crowd_general")),
                PlanItem("7天体能恢复计划", listOf("goal_health", "part_whole", "diff_beginner", "crowd_general")),
                PlanItem("快燃元气·宫吊漫跑计划", listOf("goal_cardio", "part_leg", "diff_beginner", "crowd_general")),
                PlanItem("跳绳燃脂计划", listOf("goal_fat_loss", "part_whole", "diff_elementary", "crowd_general")),
                PlanItem("高效燃脂·保持健康计划", listOf("goal_fat_loss", "goal_health", "part_whole", "diff_elementary", "crowd_general")),
                PlanItem("备战体测·高原模拟马拉松备赛", listOf("goal_cardio", "part_leg", "diff_advanced", "crowd_student"))
            )
        }

        // 获取所有筛选标签（和参考图完全一致）
        fun getAllFilterTags(): Map<FilterType, List<FilterTag>> {
            return mapOf(
                FilterType.GOAL to listOf(
                    FilterTag("goal_fat_loss", "减脂", FilterType.GOAL),
                    FilterTag("goal_muscle_gain", "增肌", FilterType.GOAL),
                    FilterTag("goal_posture", "体态改善", FilterType.GOAL),
                    FilterTag("goal_ability", "能力提升", FilterType.GOAL),
                    FilterTag("goal_relax", "舒缓放松", FilterType.GOAL),
                    FilterTag("goal_health", "保持健康", FilterType.GOAL),
                    FilterTag("goal_cardio", "有氧提升", FilterType.GOAL)
                ),
                FilterType.PART to listOf(
                    FilterTag("part_whole", "全身", FilterType.PART),
                    FilterTag("part_abdominal", "腹部", FilterType.PART),
                    FilterTag("part_waist", "腰部", FilterType.PART),
                    FilterTag("part_leg", "腿部", FilterType.PART),
                    FilterTag("part_arm", "手臂", FilterType.PART),
                    FilterTag("part_hip", "臀部", FilterType.PART),
                    FilterTag("part_back", "背部", FilterType.PART),
                    FilterTag("part_chest", "胸部", FilterType.PART),
                    FilterTag("part_neck", "颈部", FilterType.PART),
                    FilterTag("part_shoulder", "肩部", FilterType.PART)
                ),
                FilterType.DIFFICULTY to listOf(
                    FilterTag("diff_beginner", "零基础", FilterType.DIFFICULTY),
                    FilterTag("diff_elementary", "初级", FilterType.DIFFICULTY),
                    FilterTag("diff_intermediate", "进阶", FilterType.DIFFICULTY),
                    FilterTag("diff_advanced", "强化", FilterType.DIFFICULTY)
                ),
                FilterType.CROWD to listOf(
                    FilterTag("crowd_student", "学生党", FilterType.CROWD),
                    FilterTag("crowd_heavy", "大体重", FilterType.CROWD),
                    FilterTag("crowd_postpartum", "产后妈妈", FilterType.CROWD),
                    FilterTag("crowd_period", "生理期", FilterType.CROWD),
                    FilterTag("crowd_light", "小体重", FilterType.CROWD),
                    FilterTag("crowd_office_worker", "上班族", FilterType.CROWD),
                    FilterTag("crowd_middle_aged", "中老年", FilterType.CROWD)
                )
            )
        }
    }
}