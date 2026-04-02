package com.example.myapplication.data.serviceImpl

import com.example.myapplication.data.model.User
import retrofit2.http.GET

/**
 *
 * User服务接口，并不发起请求
 * */
interface UserService {
    // GET 请求示例：获取当前登录用户信息
    @GET("me")
    suspend fun me(): User

}