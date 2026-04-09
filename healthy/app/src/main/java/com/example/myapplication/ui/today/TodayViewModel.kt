package com.example.myapplication.ui.today

import android.util.Log
import androidx.lifecycle.*
import com.example.myapplication.data.model.CourseItem
import kotlinx.coroutines.launch

class TodayViewModel : ViewModel() {

    private val _courseList = MutableLiveData<List<CourseItem>>()
    val courseList: LiveData<List<CourseItem>> = _courseList

    init {
        loadDataFromBackend()
    }

    private fun loadDataFromBackend() {
        viewModelScope.launch {
            try {
                val mockData = listOf(
                    CourseItem.TrainingVideo(1, "短时间高强度腹肌训练", "KellyGale", "", "", 12, "K2"),
                    CourseItem.TrainingVideo(2, "3分钟平板支撑", "Koach-Young", "", "", 3, "K3")
                )
                _courseList.value = mockData

            } catch (e: Exception) {
                // 解决 e 从未使用
                Log.e("TodayViewModel", "获取数据失败: ${e.message}")
            }
        }
    }
}