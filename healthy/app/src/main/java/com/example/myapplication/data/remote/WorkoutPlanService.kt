package com.example.myapplication.data.remote

import com.example.myapplication.data.model.WorkoutPlanDetailDto
import com.example.myapplication.data.model.WorkoutPlanDto
import retrofit2.http.GET
import retrofit2.http.Path

interface WorkoutPlanService {

    @GET("workoutPlan/list")
    suspend fun getWorkoutPlanList(): List<WorkoutPlanDto>

    @GET("workoutPlan/{planId}/detail")
    suspend fun getWorkoutPlanDetail(@Path("planId") planId: Long): WorkoutPlanDetailDto
}
