package com.example.myapplication.ui.home.plan.planItem

import com.example.myapplication.R

// 计划实体类：预留图片、ID、接口字段
data class PlanItem(
    val id: String, // 后端接口返回的计划ID
    val name: String,
    val imageResId: Int, // 本地图片资源ID（开发阶段用）
    val imageUrl: String? = null, // 后端接口返回的图片URL（上线用）
    val tagIds: List<String>

)

{
    companion object {
        // 获取所有38个计划（已绑定图片和标签）
        fun getAllPlans(): List<PlanItem> {
            return listOf(
                PlanItem("plan_001", "个性减脂计划", R.drawable.plan_01_fat_loss, null, listOf("goal_fat_loss", "part_whole", "diff_beginner", "crowd_general")),
                PlanItem("plan_002", "告别肚腩计划", R.drawable.plan_02_abdominal, null, listOf("goal_fat_loss", "part_abdominal", "diff_beginner", "crowd_office_worker")),
                PlanItem("plan_003", "学生党·全身增肌计划", R.drawable.plan_03_student_muscle, null, listOf("goal_muscle_gain", "part_whole", "diff_beginner", "crowd_student")),
                PlanItem("plan_004", "大正爱跑步·轻松拿捏5公里…", R.drawable.plan_04_run_5km, null, listOf("goal_cardio", "part_leg", "diff_beginner", "crowd_general")),
                PlanItem("plan_005", "个性跑步计划", R.drawable.plan_05_run_custom, null, listOf("goal_cardio", "part_leg", "diff_all", "crowd_general")),
                PlanItem("plan_006", "定制大体重计划", R.drawable.plan_06_heavy_weight, null, listOf("goal_fat_loss", "part_whole", "diff_beginner", "crowd_heavy")),
                PlanItem("plan_007", "瘦腹减围·型男打造计划", R.drawable.plan_07_abs_male, null, listOf("goal_fat_loss", "part_abdominal", "diff_elementary", "crowd_general")),
                PlanItem("plan_008", "10天冲刺·极速燃脂计划", R.drawable.plan_08_sprint_10day, null, listOf("goal_fat_loss", "part_whole", "diff_elementary", "crowd_general")),
                PlanItem("plan_009", "高质量睡眠计划", R.drawable.plan_09_sleep, null, listOf("goal_relax", "part_whole", "diff_beginner", "crowd_general")),
                PlanItem("plan_010", "轻松燃脂·个性跑步计划", R.drawable.plan_10_run_fat, null, listOf("goal_fat_loss", "part_leg", "diff_beginner", "crowd_general")),
                PlanItem("plan_011", "告别脂肪胸计划", R.drawable.plan_11_chest_fat, null, listOf("goal_fat_loss", "part_chest", "diff_elementary", "crowd_male")),
                PlanItem("plan_012", "全身突击燃脂计划", R.drawable.plan_12_whole_body_burn, null, listOf("goal_fat_loss", "part_whole", "diff_intermediate", "crowd_general")),
                PlanItem("plan_013", "全身增肌·型男打造计划", R.drawable.plan_13_whole_muscle, null, listOf("goal_muscle_gain", "part_whole", "diff_elementary", "crowd_general")),
                PlanItem("plan_014", "大正爱跑步·全力奔跑5公里", R.drawable.plan_14_run_full_5km, null, listOf("goal_cardio", "part_leg", "diff_elementary", "crowd_general")),
                PlanItem("plan_015", "腹肌撕裂计划", R.drawable.plan_15_abs_tear, null, listOf("goal_muscle_gain", "part_abdominal", "diff_intermediate", "crowd_general")),
                PlanItem("plan_016", "热汗瑜伽·减脂塑形计划", R.drawable.plan_16_yoga, null, listOf("goal_fat_loss", "part_whole", "diff_beginner", "crowd_female")),
                PlanItem("plan_017", "单车智能计划", R.drawable.plan_17_cycling, null, listOf("goal_cardio", "part_leg", "diff_elementary", "crowd_general")),
                PlanItem("plan_018", "跳绳·高效燃脂计划", R.drawable.plan_18_rope_skip, null, listOf("goal_fat_loss", "part_whole", "diff_beginner", "crowd_general")),
                PlanItem("plan_019", "告别疼痛·肩颈改善计划", R.drawable.plan_19_shoulder_relief, null, listOf("goal_posture", "part_shoulder", "diff_beginner", "crowd_office_worker")),
                PlanItem("plan_020", "肩臂·强化增肌计划", R.drawable.plan_20_arm_muscle, null, listOf("goal_muscle_gain", "part_arm", "diff_intermediate", "crowd_general")),
                PlanItem("plan_021", "7天冲刺·全身燃脂计划", R.drawable.plan_21_sprint_7day, null, listOf("goal_fat_loss", "part_whole", "diff_elementary", "crowd_general")),
                PlanItem("plan_022", "定制瘦身计划", R.drawable.plan_22_slim_custom, null, listOf("goal_fat_loss", "part_whole", "diff_beginner", "crowd_general")),
                PlanItem("plan_023", "经典胸肩·强效增肌计划", R.drawable.plan_23_chest_shoulder, null, listOf("goal_muscle_gain", "part_chest", "diff_intermediate", "crowd_general")),
                PlanItem("plan_024", "7天瘦全身·晚安燃脂计划", R.drawable.plan_24_night_burn, null, listOf("goal_fat_loss", "part_whole", "diff_beginner", "crowd_office_worker")),
                PlanItem("plan_025", "学生专属·瘦全身计划", R.drawable.plan_25_student_slim, null, listOf("goal_fat_loss", "part_whole", "diff_beginner", "crowd_student")),
                PlanItem("plan_026", "肩臂减脂计划", R.drawable.plan_26_arm_fat, null, listOf("goal_fat_loss", "part_arm", "diff_beginner", "crowd_general")),
                PlanItem("plan_027", "科林滚滚·搏击有氧计划", R.drawable.plan_27_boxing, null, listOf("goal_fat_loss", "part_whole", "diff_elementary", "crowd_general")),
                PlanItem("plan_028", "学生升学生涯减脂计划", R.drawable.plan_28_student_exam, null, listOf("goal_fat_loss", "part_whole", "diff_beginner", "crowd_student")),
                PlanItem("plan_029", "哑铃上肢增肌计划", R.drawable.plan_29_dumbbell, null, listOf("goal_muscle_gain", "part_arm", "diff_elementary", "crowd_general")),
                PlanItem("plan_030", "马甲线控制·会员塑造计划", R.drawable.plan_30_vest_line, null, listOf("goal_fat_loss", "part_abdominal", "diff_intermediate", "crowd_general")),
                PlanItem("plan_031", "学生燃脂计划", R.drawable.plan_31_student_burn, null, listOf("goal_fat_loss", "part_whole", "diff_beginner", "crowd_student")),
                PlanItem("plan_032", "优质增肌雕刻计划", R.drawable.plan_32_muscle_sculpt, null, listOf("goal_muscle_gain", "part_whole", "diff_advanced", "crowd_general")),
                PlanItem("plan_033", "21天健康体质增强计划", R.drawable.plan_33_health_21day, null, listOf("goal_health", "part_whole", "diff_beginner", "crowd_general")),
                PlanItem("plan_034", "7天体能恢复计划", R.drawable.plan_34_recover_7day, null, listOf("goal_health", "part_whole", "diff_beginner", "crowd_general")),
                PlanItem("plan_035", "快燃元气·宫吊漫跑计划", R.drawable.plan_35_jog_gong, null, listOf("goal_cardio", "part_leg", "diff_beginner", "crowd_general")),
                PlanItem("plan_036", "跳绳燃脂计划", R.drawable.plan_36_rope_burn, null, listOf("goal_fat_loss", "part_whole", "diff_elementary", "crowd_general")),
                PlanItem("plan_037", "高效燃脂·保持健康计划", R.drawable.plan_37_burn_health, null, listOf("goal_fat_loss", "goal_health", "part_whole", "diff_elementary", "crowd_general")),
                PlanItem("plan_038", "备战体测·高原模拟马拉松备赛", R.drawable.plan_38_marathon, null, listOf("goal_cardio", "part_leg", "diff_advanced", "crowd_student"))
            )
        }

        // 获取所有筛选标签（保持不变）
        fun getAllFilterTags(): Map<FilterType, List<FilterTag>> {
            return mapOf(
                FilterType.GOAL to listOf(
                    FilterTag("goal_fat_loss", "减脂", FilterType.GOAL),
                    FilterTag("goal_muscle_gain", "增肌", FilterType.GOAL),
                    FilterTag("goal_posture", "体态改善", FilterType.GOAL),
                    FilterTag("goal_relax", "舒缓放松", FilterType.GOAL),
                    FilterTag("goal_health", "保持健康", FilterType.GOAL),
                    FilterTag("goal_cardio", "有氧提升", FilterType.GOAL)
                ),
                FilterType.PART to listOf(
                    FilterTag("part_whole", "全身", FilterType.PART),
                    FilterTag("part_abdominal", "腹部", FilterType.PART),
                    FilterTag("part_leg", "腿部", FilterType.PART),
                    FilterTag("part_arm", "手臂", FilterType.PART),
                    FilterTag("part_chest", "胸部", FilterType.PART),
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
                    FilterTag("crowd_office_worker", "上班族", FilterType.CROWD),
                )
            )
        }
    }
}