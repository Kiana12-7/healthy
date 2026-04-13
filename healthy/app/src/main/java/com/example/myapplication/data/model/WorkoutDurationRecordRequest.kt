package com.example.myapplication.data.model

data class WorkoutDurationRecordRequest(
    val recordDate: String,
    val durationSeconds: Int,
    val sourceType: String
)
