package com.example.myapplication.data.remote

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface FitnessFormService {
    @POST("fitnessForm/save")
    fun save(@Body description: String): Call<ResponseBody>
}