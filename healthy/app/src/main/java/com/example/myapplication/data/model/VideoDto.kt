package com.example.myapplication.data.model

import com.google.gson.annotations.SerializedName

/**
 * 对应后端的 VideoDto
 */
data class VideoDto(
    val id: Int,
    val title: String,       // 标题
    val author: String?,     // 作者 (对应后端的 author)
    val coverUrl: String?,   // 封面
    val videoUrl: String,    // 视频地址
    val duration: Int,       // 时长
    val level: String?,       // 等级 (对应后端的 level)
    val preparePose: String?,
    val actionProcess: String?,
    val breathRhythm: String?,
    val attention: String?
)