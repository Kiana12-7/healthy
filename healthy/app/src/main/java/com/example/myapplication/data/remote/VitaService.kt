package com.example.myapplication.data.remote

import com.example.myapplication.data.model.AIChatRequest
import com.example.myapplication.data.model.AIChatResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Body
import retrofit2.http.POST

interface VitaService {
    // 获取当前登录用户信息
    @GET("vita/generatePlan")
    suspend fun generatePlan(): Response<Unit>

    @POST("vita/chat")
    suspend fun chat(@Body request: AIChatRequest): AIChatResponse
}
