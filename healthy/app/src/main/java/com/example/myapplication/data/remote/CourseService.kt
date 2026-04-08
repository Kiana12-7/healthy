package com.example.myapplication.data.remote

import com.example.myapplication.data.model.CourseItem


import retrofit2.Call
import retrofit2.http.GET

interface CourseService {
    @GET("course/list")
    fun getVideoList(): Call<List<CourseItem.TrainingVideo>>
}