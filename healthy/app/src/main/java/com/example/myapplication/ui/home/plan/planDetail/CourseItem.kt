package com.example.myapplication.ui.home.plan.planDetail

import java.io.Serializable

data class CourseItem(
    val courseId: String,
    val planId: String,
    val courseName: String,     // 列表页显示的短名称
    val content: String,        // 详情页显示的完整训练步骤（分条）
    val duration: Int,          // 训练总时长（分钟）
    val difficulty: String,     // 难度：易/中/难
    val isLearned: Boolean,
    val videoUrl: String,       // 保留，后续可加视频入口
    val coverUrl: String? = null
) : Serializable

// 计划详情实体类
data class PlanDetail(
    val planId: String,
    val planName: String,
    val totalCourseCount: Int,
    val courseList: List<CourseItem>
)
