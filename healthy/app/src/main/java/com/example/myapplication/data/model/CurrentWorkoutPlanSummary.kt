package com.example.myapplication.data.model

data class CurrentWorkoutPlanSummary(
    val hasActivePlan: Boolean,
    val planId: Long?,
    val planName: String?,
    val startDate: String?,
    val endDate: String?,
    val currentDay: Int?,
    val totalDays: Int?
)
