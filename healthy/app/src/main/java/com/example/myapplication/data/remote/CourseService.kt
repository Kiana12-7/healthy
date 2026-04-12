package com.example.myapplication.data.remote

import com.example.myapplication.data.model.VideoDto // 导入刚才创建的 Dto
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * 课程相关服务接口
 */
interface CourseService {

    // 1. 修改返回类型为 VideoDto，这才是后端真实传过来的数据结构
    // 2. 增加 @Query 参数，为之后联动“真实时间”做准备
    @GET("course/list")
    suspend fun getVideoList(
        @Query("date") date: String? = null
    ): List<VideoDto>
}