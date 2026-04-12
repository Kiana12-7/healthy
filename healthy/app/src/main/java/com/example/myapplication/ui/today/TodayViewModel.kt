package com.example.myapplication.ui.today

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.model.CalendarDay
import com.example.myapplication.data.remote.RetrofitClient
import com.example.myapplication.ui.home.plan.planDetail.CourseItem as PlanCourseItem
import com.example.myapplication.ui.home.plan.planDetail.TrainActionItem
import com.example.myapplication.data.model.CourseItem as VideoCourseItem
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class TodayViewModel : ViewModel() {
    private var currentSelectedDate: Date = Date()

    private val _calendarDays = MutableLiveData<List<CalendarDay>>()
    val calendarDays: LiveData<List<CalendarDay>> = _calendarDays

    private val _scrollToPosition = MutableLiveData<Int>()
    val scrollToPosition: LiveData<Int> = _scrollToPosition

    private val _todayPlanList = MutableLiveData<List<PlanCourseItem>>()
    val todayPlanList: LiveData<List<PlanCourseItem>> = _todayPlanList

    private val _courseList = MutableLiveData<List<VideoCourseItem>>()
    val courseList: LiveData<List<VideoCourseItem>> = _courseList

    val isLoading = MutableLiveData<Boolean>()

    init {
        generateCalendarRange()
        refreshCurrentDate()
    }

    private fun generateCalendarRange() {
        val daysList = mutableListOf<CalendarDay>()
        val calendar = Calendar.getInstance()

        // 记录今天的日期
        val todayYear = calendar.get(Calendar.YEAR)
        val todayDayOfYear = calendar.get(Calendar.DAY_OF_YEAR)

        // 往前推 15 天
        calendar.add(Calendar.DAY_OF_YEAR, -15)

        val monthDayFormat = SimpleDateFormat("M/d", Locale.getDefault())
        val weekFormat = SimpleDateFormat("E", Locale.CHINESE)

        var todayIndex = 0
        for (i in 0..30) {
            val isToday = calendar.get(Calendar.YEAR) == todayYear &&
                    calendar.get(Calendar.DAY_OF_YEAR) == todayDayOfYear

            if (isToday) todayIndex = i // 锁定今天在 31 天里的位置

            daysList.add(
                CalendarDay(
                    date = calendar.time,
                    displayMonthDay = monthDayFormat.format(calendar.time),
                    displayWeekDay = weekFormat.format(calendar.time),
                    isToday = isToday,
                    isSelected = isToday
                )
            )
            calendar.add(Calendar.DAY_OF_YEAR, 1)
        }

        _calendarDays.value = daysList

        _scrollToPosition.value = todayIndex
    }

    private fun fetchTodayPlanFromServer(date: Date) {
        val dateParam = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(date)
        viewModelScope.launch {
            isLoading.value = true
            try {
                val response = RetrofitClient.workoutPlanService.getTodayWorkoutPlanCourses(dateParam)
                _todayPlanList.value = response.map { dto ->
                    PlanCourseItem(
                        courseId = dto.courseId,
                        planId = dto.planId.toString(),
                        courseName = listOfNotNull(dto.planName, dto.courseName).joinToString(" · "),
                        actionList = dto.actionList.map { action ->
                            TrainActionItem(
                                actionId = action.actionId.toString(),
                                actionName = action.actionName,
                                groupDesc = action.groupDesc,
                                restDesc = action.restDesc,
                                videoUrl = action.videoUrl,
                                actionDesc = action.actionDesc
                            )
                        },
                        duration = dto.duration,
                        difficulty = dto.difficulty,
                        isLearned = dto.learned,
                        videoUrl = dto.videoUrl,
                        coverUrl = dto.coverUrl
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _todayPlanList.value = emptyList()
            } finally {
                isLoading.value = false
            }
        }
    }

    private fun fetchCoursesFromServer(date: Date) {
        val dateParam = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(date)
        viewModelScope.launch {
            try {
                val response = RetrofitClient.courseService.getVideoList(dateParam)
                val mappedList = response.map { dto ->
                    VideoCourseItem.TrainingVideo(
                        id = dto.id,
                        title = dto.title,
                        trainerName = dto.author ?: "专业教练",
                        coverUrl = dto.coverUrl ?: "",
                        videoUrl = dto.videoUrl,
                        duration = dto.duration,
                        difficultyTag = dto.level ?: "初级",
                        preparePose = dto.preparePose ?: "自然站立，核心收紧",
                        actionProcess = dto.actionProcess ?: "保持动作稳定，匀速完成",
                        breathRhythm = dto.breathRhythm ?: "保持均匀呼吸",
                        attention = dto.attention ?: "动作标准优先，不要追求速度"
                    )
                }
                _courseList.value = mappedList
            } catch (e: Exception) {
                e.printStackTrace()
                _courseList.value = emptyList()
            }
        }
    }

    fun onDateSelected(selectedDay: CalendarDay, position: Int) {
        currentSelectedDate = selectedDay.date
        val currentList = _calendarDays.value ?: return
        val newList = currentList.map {
            it.copy(isSelected = (it.date == selectedDay.date))
        }
        _calendarDays.value = newList
        _scrollToPosition.value = position
        fetchTodayPlanFromServer(selectedDay.date)
        fetchCoursesFromServer(selectedDay.date)
    }

    fun selectDateFromPicker(year: Int, month: Int, dayOfMonth: Int) {
        val targetCalendar = Calendar.getInstance()
        targetCalendar.set(year, month, dayOfMonth)
        currentSelectedDate = targetCalendar.time
        val targetDateStr = SimpleDateFormat("M/d", Locale.getDefault()).format(targetCalendar.time)
        val currentList = _calendarDays.value ?: return
        val targetIndex = currentList.indexOfFirst { it.displayMonthDay == targetDateStr }

        if (targetIndex != -1) {
            onDateSelected(currentList[targetIndex], targetIndex)
        } else {
            fetchTodayPlanFromServer(targetCalendar.time)
            fetchCoursesFromServer(targetCalendar.time)
        }
    }

    fun refreshCurrentDate() {
        fetchTodayPlanFromServer(currentSelectedDate)
        fetchCoursesFromServer(currentSelectedDate)
    }
}
