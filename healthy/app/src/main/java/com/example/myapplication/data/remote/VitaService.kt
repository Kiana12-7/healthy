package com.example.myapplication.data.remote

import retrofit2.Response
import retrofit2.http.GET

interface VitaService {
    // 获取当前登录用户信息
    @GET("vita/generatePlan")
    suspend fun generatePlan(): Response<Unit>

}