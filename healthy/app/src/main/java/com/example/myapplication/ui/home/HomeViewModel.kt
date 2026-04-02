package com.example.myapplication.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.data.models.HomeItem

class HomeViewModel : ViewModel() {

    private val _homeData = MutableLiveData<List<HomeItem.Video>>()
    val homeData: LiveData<List<HomeItem.Video>> = _homeData

    // 建议在初始化时就加载，防止 Fragment 观察时数据还没准备好
    init {
        fetchHomeData()
    }

    // HomeViewModel.kt
    fun fetchHomeData() {
        val data = listOf(
            HomeItem.Video(1, "超模核心训练", "KellyGale", "https://picsum.photos/400/300", "https://vjs.zencdn.net/v/oceans.mp4", "28分钟", "K3"),
            HomeItem.Video(2, "快乐燃脂·暴汗全身", "Koach-Young", "https://picsum.photos/400/301", "https://media.w3.org/2010/05/sintel/trailer.mp4", "15分钟", "K1"),
            HomeItem.Video(3, "HIIT全身进阶", "Alex", "https://picsum.photos/400/302", "https://www.w3schools.com/html/mov_bbb.mp4", "20分钟", "K2")
        )
        _homeData.postValue(data)
    }
}