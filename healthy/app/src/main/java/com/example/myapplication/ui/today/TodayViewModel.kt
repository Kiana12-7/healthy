package com.example.myapplication.ui.today

import android.util.Log
import androidx.lifecycle.*
import com.example.myapplication.data.model.CalendarDay // 确保这个路径是对的
import com.example.myapplication.data.model.CourseItem
import com.example.myapplication.data.remote.RetrofitClient // 确保这个路径是对的
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class TodayViewModel : ViewModel() {

    private val _courseList = MutableLiveData<List<CourseItem>>()
    val courseList: LiveData<List<CourseItem>> = _courseList

    private val _calendarDays = MutableLiveData<List<CalendarDay>>()
    val calendarDays: LiveData<List<CalendarDay>> = _calendarDays

    private val _selectedDate = MutableLiveData<CalendarDay>()

    init {
        generateCalendarDays()
    }

    private fun generateCalendarDays() {
        val daysList = mutableListOf<CalendarDay>()
        val calendar = Calendar.getInstance()
        val dayOfWeekFormat = SimpleDateFormat("E", Locale.CHINA)
        val dayOfMonthFormat = SimpleDateFormat("dd", Locale.getDefault())

        for (i in 0 until 14) {
            val date = calendar.time
            val isSelected = (i == 0)

            val day = CalendarDay(
                date = date,
                dayOfWeek = dayOfWeekFormat.format(date),
                dayOfMonth = dayOfMonthFormat.format(date),
                isSelected = isSelected
            )
            daysList.add(day)

            if (isSelected) {
                _selectedDate.value = day
            }
            calendar.add(Calendar.DAY_OF_YEAR, 1)
        }

        _calendarDays.value = daysList
        loadDataFromBackend()
    }

    fun onDateSelected(selectedDay: CalendarDay) {
        // 关键修复：从 LiveData 中取出 List，并使用 List.map 而非 LiveData.map
        val currentList = _calendarDays.value ?: return

        val newList = currentList.map {
            // 确保 CalendarDay 是 data class 才能使用 .copy
            it.copy(isSelected = it.date == selectedDay.date)
        }

        // 重新赋值给 LiveData
        _calendarDays.value = newList
        _selectedDate.value = selectedDay

        loadDataFromBackend()
    }

    private fun loadDataFromBackend() {
        viewModelScope.launch {
            try {
                // 如果报错 Unresolved reference 'RetrofitClient'，
                // 请检查 RetrofitClient.kt 的 package 声明是否正确
                val result = RetrofitClient.courseService.getVideoList()
                _courseList.value = result
            } catch (e: Exception) {
                Log.e("TodayViewModel", "Error: ${e.message}")
                _courseList.value = emptyList()
            }
        }
    }
}