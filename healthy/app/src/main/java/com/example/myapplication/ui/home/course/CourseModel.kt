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

                // 2. 将 List<VideoDto> 转换为 List<CourseItem.TrainingVideo>
                val mappedList = list.map { dto ->
                    CourseItem.TrainingVideo(
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