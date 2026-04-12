package com.example.myapplication.data.remote

import android.content.Context
import com.example.myapplication.utils.OkHttpUtil
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "http://10.0.2.2:8080/"

    // 用来传入上下文
    private var appContext: Context? = null

    fun init(context: Context) {
        appContext = context.applicationContext
    }

    private val okHttpClient: OkHttpClient by lazy {
        OkHttpUtil.getClientWithLogging(appContext!!)
    }

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient) // 绑定
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val fitnessFormService: FitnessFormService by lazy {
        retrofit.create(FitnessFormService::class.java)
    }
    val vitaService: VitaService by lazy {
        retrofit.create(VitaService::class.java)
    }

    val userService: UserService by lazy {
        retrofit.create(UserService::class.java)
    }

    val courseService: CourseService by lazy {
        retrofit.create(CourseService::class.java)
    }

    val workoutPlanService: WorkoutPlanService by lazy {
        retrofit.create(WorkoutPlanService::class.java)
    }
}
