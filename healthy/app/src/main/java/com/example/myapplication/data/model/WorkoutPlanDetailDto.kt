package com.example.myapplication.data.model

data class WorkoutPlanDetailDto(
    val planId: Long,
    val planName: String,
    val totalCourseCount: Int,
    val courseList: List<WorkoutPlanCourseDto>
)

data class WorkoutPlanCourseDto(
    val courseId: String,
    val planId: Long,
    val planName: String? = null,
    val courseName: String,
    val actionList: List<WorkoutPlanActionDto>,
    val duration: Int,
    val difficulty: String,
    val learned: Boolean,
    val videoUrl: String,
    val coverUrl: String? = null
)

data class WorkoutPlanActionDto(
    val actionId: Long,
    val actionName: String,
    val groupDesc: String,
    val restDesc: String,
    val videoUrl: String,
    val actionDesc: String
)
