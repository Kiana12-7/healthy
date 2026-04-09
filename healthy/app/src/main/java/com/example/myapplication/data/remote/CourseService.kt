package com.example.myapplication.data.remote

import com.example.myapplication.data.model.CourseItem
import retrofit2.http.GET

/**
 * 课程相关服务接口
 */
interface CourseService {

    // 使用 suspend 关键字配合协程
    // 确保返回类型与最新的 CourseItem 结构对齐
    @GET("course/list")
    suspend fun getVideoList(): List<CourseItem.TrainingVideo>
}