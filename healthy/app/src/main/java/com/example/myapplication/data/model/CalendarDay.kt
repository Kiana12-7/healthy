package com.example.myapplication.data.model

import java.util.Date

data class CalendarDay(
    val date: Date,            // 真实日期对象
    val displayMonthDay: String, // 显示用，例如 "4/9"
    val displayWeekDay: String,  // 显示用，例如 "周四"
    val isToday: Boolean,        // 是否是今天
    var isSelected: Boolean      // 是否被选中
)