package com.example.myapplication

import android.app.Application
import com.example.myapplication.data.remote.RetrofitClient

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        // 初始化 Retrofit + Cookie
        RetrofitClient.init(this)
    }
}