package com.example.myapplication.ui.today

import android.util.Log
import androidx.lifecycle.*
import com.example.myapplication.data.model.HomeItem
import kotlinx.coroutines.launch

class TodayViewModel : ViewModel() {

    private val _courseList = MutableLiveData<List<HomeItem>>()
    val courseList: LiveData<List<HomeItem>> = _courseList

    init {
        loadDataFromBackend()
    }

    private fun loadDataFromBackend() {
        viewModelScope.launch {
            try {
                // TODO: 等后端写好后在这里调用 RetrofitClient.userService.getVideos()

                // 【模拟后端返回数据】解决 HomeItem 实例化报错
                val mockData = listOf(
                    HomeItem.Video(1, "短时间高强度腹肌训练", "KellyGale", "", "", "12分钟", "K2"),
                    HomeItem.Video(2, "3分钟平板支撑", "Koach-Young", "", "", "3分钟", "K3")
                )
                _courseList.value = mockData

            } catch (e: Exception) {
                // 解决 e 从未使用
                Log.e("TodayViewModel", "获取数据失败: ${e.message}")
            }
        }
    }

    fun askAiCoach(msg: String) {
        // 解决 msg 从未使用
        Log.d("AI_COACH", "收到提问: $msg")
    }
}