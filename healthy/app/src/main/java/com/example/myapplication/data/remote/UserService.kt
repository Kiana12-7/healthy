package com.example.myapplication.data.remote

import com.example.myapplication.data.model.LoggedInUser
import com.example.myapplication.data.model.User
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

/**
 * User 服务接口
 * 并不发起请求
 **/
interface UserService {
    // 获取当前登录用户信息
    @GET("me")
    suspend fun me(): User

    @FormUrlEncoded
    @POST("user/login")
    suspend fun login(
        @Field("username") username: String,
        @Field("password") password: String
    ): LoggedInUser

}