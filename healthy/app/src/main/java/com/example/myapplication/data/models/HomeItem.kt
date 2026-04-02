package com.example.myapplication.data.models

// HomeItem.kt
sealed class HomeItem {
    data class Video(
        val id: Int,
        val title: String,
        val author: String,
        val coverUrl: String,
        val videoUrl: String, // 播放网址
        val duration: String,
        val tag: String
    ) : HomeItem()
}