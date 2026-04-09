package com.example.myapplication.ui.home.course

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.model.CourseItem
import com.example.myapplication.data.remote.RetrofitClient
import kotlinx.coroutines.launch

class CourseModel : ViewModel() {
    private val _homeData = MutableLiveData<List<CourseItem.TrainingVideo>>()
    val homeData: LiveData<List<CourseItem.TrainingVideo>> = _homeData

    init {
        fetchVideoListFromServer()
    }

    fun fetchVideoListFromServer() {
        // 使用协程，不再需要 enqueue 和 Callback
        viewModelScope.launch {
            try {
                // 直接调用 suspend 方法，它会挂起协程直到获取结果
                val list = RetrofitClient.courseService.getVideoList()

                // 协程中可以直接使用 .value 赋值（如果在主线程），
                // 或者为了安全起见使用 .postValue
                _homeData.value = list

                Log.d("CourseModel", "成功获取数据，数量: ${list.size}")
            } catch (e: Exception) {
                // 处理异常（如网络断开、服务器 404 等）
                Log.e("CourseModel", "获取视频列表失败: ${e.message}")
                _homeData.value = emptyList()
            }
        }
    }
}