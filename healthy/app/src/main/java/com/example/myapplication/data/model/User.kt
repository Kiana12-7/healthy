package com.example.myapplication.data.model

data class User(
    val id: Int,             // 用户ID
    val name: String,        // 用户名
    val days: Int,           // 运动天数
    val calories: Int,       // 消耗卡路里
    val courses: Int         // 完成课程数
)