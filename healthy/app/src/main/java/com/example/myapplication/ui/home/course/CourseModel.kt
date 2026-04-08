package com.example.myapplication.ui.home.course

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.model.CourseItem
import com.example.myapplication.data.remote.RetrofitClient
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CourseModel : ViewModel() {
    private val _homeData = MutableLiveData<List<CourseItem.TrainingVideo>>()
    val homeData: LiveData<List<CourseItem.TrainingVideo>> = _homeData

    init {
        fetchVideoListFromServer()
    }

    fun fetchVideoListFromServer() {
        viewModelScope.launch {
            RetrofitClient.courseService.getVideoList()
                .enqueue(object : Callback<List<CourseItem.TrainingVideo>> {
                    override fun onResponse(
                        call: Call<List<CourseItem.TrainingVideo>>,
                        response: Response<List<CourseItem.TrainingVideo>>
                    ) {
                        val list = response.body() ?: emptyList()
                        _homeData.postValue(list)
                    }

                    override fun onFailure(call: Call<List<CourseItem.TrainingVideo>>, t: Throwable) {
                        t.printStackTrace()
                    }
                })
        }
    }
}