package com.example.myapplication.data.model

data class WorkoutDurationDailyStat(
    val recordDate: String,
    val planDurationSeconds: Int,
    val aiPlanDurationSeconds: Int,
    val totalDurationSeconds: Int
)
