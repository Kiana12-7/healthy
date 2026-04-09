package com.example.myapplication.ui.today

import android.util.Log
import androidx.lifecycle.*
import com.example.myapplication.data.model.CourseItem
import com.example.myapplication.data.remote.RetrofitClient
import kotlinx.coroutines.launch

class TodayViewModel : ViewModel() {

    // 1. 显式声明 LiveData 类型，确保 Fragment 观察时类型推断正确
    private val _courseList = MutableLiveData<List<CourseItem>>()
    val courseList: LiveData<List<CourseItem>> = _courseList

    init {
        loadDataFromBackend()
    }

    /**
     * 核心逻辑：从数据库获取真实数据
     */
    fun loadDataFromBackend() {
        // 使用 viewModelScope 需要在 build.gradle 中包含 lifecycle-viewmodel-ktx
        viewModelScope.launch {
            try {
                // 调用 RetrofitClient 中的 CourseService
                // 这里得到的将是 List<CourseItem.TrainingVideo>
                val result = RetrofitClient.courseService.getVideoList()

                // 更新 LiveData，通知 UI 刷新
                _courseList.value = result

                Log.d("TodayViewModel", "成功从后端数据库加载 ${result.size} 条数据")

            } catch (e: Exception) {
                // 解决 e 从未使用，并记录真实的错误信息
                Log.e("TodayViewModel", "数据库连接或请求失败: ${e.localizedMessage}")

                // 失败时给一个空列表，防止 UI 层面出现 null 异常
                _courseList.value = emptyList()
            }
        }
    }
}