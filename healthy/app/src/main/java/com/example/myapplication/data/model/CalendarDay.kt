package com.example.myapplication.data.model

import java.util.Date

/**
 * 日历日期模型
 */
data class CalendarDay(
    val date: Date,          // 原始日期对象
    val dayOfWeek: String,   // 星期几 (如: 周一)
    val dayOfMonth: String,  // 几号 (如: 09)
    var isSelected: Boolean = false // 是否被选中
)