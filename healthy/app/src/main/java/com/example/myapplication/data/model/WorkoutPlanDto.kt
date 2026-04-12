package com.example.myapplication.data.model

data class WorkoutPlanDto(
    val id: Long,
    val name: String,
    val startDate: String? = null,
    val endDate: String? = null
)
