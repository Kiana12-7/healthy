package com.example.myapplication.data.model

data class WorkoutDurationSummary(
    val totalDurationSeconds: Int,
    val planDurationSeconds: Int,
    val aiPlanDurationSeconds: Int,
    val activeDays: Int,
    val dailyRecords: List<WorkoutDurationDailyStat>
)
