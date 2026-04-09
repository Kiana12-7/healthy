package com.example.myapplication.ui.home.plan.planItem

// 课程实体类
data class CourseItem(
    val courseId: String,
    val planId: String,
    val courseName: String,
    val duration: Int,
    val difficulty: String,
    val isLearned: Boolean,
    val videoUrl: String,
    val coverUrl: String? = null
)

// 计划详情实体类
data class PlanDetail(
    val planId: String,
    val planName: String,
    val totalCourseCount: Int,
    val totalUserCount: Int,
    val courseList: List<CourseItem>
)