package com.example.myapplication.ui.home.plan.planDetail

import java.io.Serializable

data class CourseItem(
    val courseId: String,
    val planId: String,
    val courseName: String,
    val actionList: List<TrainActionItem>,
    val duration: Int,
    val difficulty: String,
    val isLearned: Boolean,
    val videoUrl: String,
    val coverUrl: String? = null
) : Serializable

// 计划详情实体类
data class PlanDetail(
    val planId: String,
    val planName: String,
    val totalCourseCount: Int,
    val courseList: List<CourseItem>
)
// 单个训练动作实体（对应卡片）
data class TrainActionItem(
    val actionId: String,        // 【核心】对应后端Video表的视频ID，后续对接接口直接用
    val actionName: String,      // 动作名称（开合跳、深蹲等）
    val groupDesc: String,       // 组数次数描述（20次×4组）
    val restDesc: String,        // 休息描述（组间休息45秒）
    val videoUrl: String,        // 动作教学视频地址（复用你现有的视频地址）
    val actionDesc: String       // 完整动作要点（弹窗里显示）
) : Serializable