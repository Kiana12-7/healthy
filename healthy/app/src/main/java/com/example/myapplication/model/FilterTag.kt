package com.example.myapplication.model

// 筛选类型枚举
enum class FilterType {
    GOAL, // 目标
    PART, // 部位
    DIFFICULTY, // 难度
    CROWD // 适合人群
}

// 筛选标签实体类
data class FilterTag(
    val id: String,
    val name: String,
    val type: FilterType,
    var isSelected: Boolean = false
)