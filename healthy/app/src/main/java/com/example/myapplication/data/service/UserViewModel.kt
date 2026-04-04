package com.example.myapplication.data.service

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.model.User
import com.example.myapplication.data.serviceImpl.RetrofitClient
import kotlinx.coroutines.launch

/**
 *
 * 类似服务端，用于c层调用
 * */
class UserViewModel: ViewModel() {
    // 状态流，用于向 UI 层暴露数据
    private val _user = MutableLiveData<User?>(null)
    val user: LiveData<User?> = _user as LiveData<User?>


    fun me() {
        viewModelScope.launch {
            try {
                _user.value = RetrofitClient.userService.me()
                print(_user.value);
            } catch (e: Exception) {
                // 网络异常或其他错误
                e.printStackTrace()
            }
        }
    }
}