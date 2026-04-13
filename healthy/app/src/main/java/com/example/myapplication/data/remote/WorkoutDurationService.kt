package com.example.myapplication.data.remote

import com.example.myapplication.data.model.WorkoutDurationRecordRequest
import com.example.myapplication.data.model.WorkoutDurationSummary
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface WorkoutDurationService {
    @POST("workoutDuration/record")
    suspend fun recordDuration(@Body request: WorkoutDurationRecordRequest)

    @GET("workoutDuration/summary")
    suspend fun getSummary(
        @Query("startDate") startDate: String,
        @Query("endDate") endDate: String
    ): WorkoutDurationSummary
}
