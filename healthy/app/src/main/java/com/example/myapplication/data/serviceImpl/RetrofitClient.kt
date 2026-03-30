package com.example.myapplication.data.serviceImpl

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.lazy
import okhttp3.logging.HttpLoggingInterceptor

object RetrofitClient {
    // 替换为你的后台接口地址
    private const val BASE_URL = "http://10.0.2.2:8080/user/"

    // OkHttp 客户端（带日志）
    private val okHttpClient by lazy {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }

    // Retrofit 实例
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // 提供 ApiService 实例
    val userService: UserService by lazy {
        retrofit.create(UserService::class.java)
    }
}