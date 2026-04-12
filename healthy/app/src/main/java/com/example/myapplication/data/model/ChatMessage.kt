package com.example.myapplication.data.model

/**
 * 聊天消息模型
 */
data class ChatMessage(
    val content: String,
    val isFromUser: Boolean, // true 为用户发送（显示在右侧），false 为 AI 发送（显示在左侧）
    val timestamp: Long = System.currentTimeMillis()
)