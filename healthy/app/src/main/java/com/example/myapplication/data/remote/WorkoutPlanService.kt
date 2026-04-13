package com.example.myapplication.data.remote

import com.example.myapplication.data.model.CurrentWorkoutPlanSummary
import com.example.myapplication.data.model.WorkoutPlanDetailDto
import com.example.myapplication.data.model.WorkoutPlanDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface WorkoutPlanService {

    @GET("workoutPlan/list")
    suspend fun getWorkoutPlanList(): List<WorkoutPlanDto>

    @GET("workoutPlan/{planId}/detail")
    suspend fun getWorkoutPlanDetail(@Path("planId") planId: Long): WorkoutPlanDetailDto

    @GET("workoutPlan/today")
    suspend fun getTodayWorkoutPlanCourses(@Query("date") date: String): List<com.example.myapplication.data.model.WorkoutPlanCourseDto>

    @GET("workoutPlan/currentSummary")
    suspend fun getCurrentPlanSummary(@Query("date") date: String): CurrentWorkoutPlanSummary
}
