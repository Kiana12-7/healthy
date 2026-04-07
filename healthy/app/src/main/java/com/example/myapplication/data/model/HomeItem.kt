package com.example.myapplication.data.model

// HomeItem.kt
sealed class HomeItem {
    // 定义一个抽象 id，方便 Adapter 处理
    abstract val id: Int

    data class Video(
        override val id: Int,
        val title: String,
        val author: String,
        val coverUrl: String,
        val videoUrl: String,
        val duration: String,
        val tag: String
    ) : HomeItem()

    // 如果以后有日程类型，可以加在这里
    // data class Schedule(override val id: Int, val task: String) : HomeItem()
}