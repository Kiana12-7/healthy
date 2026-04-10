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
        viewModelScope.launch {
            try {
                // 1. 获取后端原始数据 List<VideoDto>
                val list = RetrofitClient.courseService.getVideoList()

                // 2. 【核心修复】将 List<VideoDto> 转换为 List<CourseItem.TrainingVideo>
                // 解决 Assignment type mismatch 报错
                val mappedList = list.map { dto ->
                    CourseItem.TrainingVideo(
                        id = dto.id,
                        title = dto.title,
                        trainerName = dto.author ?: "专业教练", // 对应后端的 author
                        coverUrl = dto.coverUrl ?: "",
                        videoUrl = dto.videoUrl,
                        duration = dto.duration,
                        difficultyTag = dto.level ?: "初级"     // 对应后端的 level
                    )
                }

                // 3. 将转换后的 UI 列表赋值给 LiveData
                _homeData.value = mappedList

                Log.d("CourseModel", "成功获取数据，数量: ${mappedList.size}")
            } catch (e: Exception) {
                Log.e("CourseModel", "获取视频列表失败: ${e.message}")
                _homeData.value = emptyList()
            }
        }
    }
}