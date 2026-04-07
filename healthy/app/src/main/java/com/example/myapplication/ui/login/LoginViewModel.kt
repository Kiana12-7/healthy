package com.example.myapplication.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.util.Patterns
import com.example.myapplication.data.repository.LoginRepository
import com.example.myapplication.data.model.Result

import com.example.myapplication.R

class LoginViewModel(private val loginRepository: LoginRepository) : ViewModel() {
    // 内部可修改的表单状态（私有）
    private val _loginForm = MutableLiveData<LoginFormState>()
    // 页面外部只能观察，不能修改
    val loginFormState: LiveData<LoginFormState> = _loginForm
    // 内部可修改的登录结果
    private val _loginResult = MutableLiveData<LoginResult>()
    // 页面外部观察登录结果
    val loginResult: LiveData<LoginResult> = _loginResult

    /**
     * 执行登录
     * @param username 用户名
     * @param password 密码
     */
    suspend fun login(username: String, password: String) {
        // can be launched in a separate asynchronous job
        val result = loginRepository.login(username, password)

        if (result is Result.Success) {
            _loginResult.value =
                LoginResult(success = LoggedInUserView(displayName = result.data.displayName))
        } else {
            _loginResult.value = LoginResult(error = R.string.login_failed)
        }
    }

    /**
     * 当用户名/密码输入内容变化时触发
     * 用于实时校验输入是否合法
     */
    fun loginDataChanged(username: String, password: String) {
        if (!isUserNameValid(username)) {
            _loginForm.value = LoginFormState(usernameError = R.string.invalid_username)
        } else if (!isPasswordValid(password)) {
            _loginForm.value = LoginFormState(passwordError = R.string.invalid_password)
        } else {
            _loginForm.value = LoginFormState(isDataValid = true)
        }
    }

    /**
     * 用户名合法性校验
     * 规则：如果包含 @ 则按邮箱校验，否则非空即有效
     */
    private fun isUserNameValid(username: String): Boolean {
        return if (username.contains('@')) {
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else {
            username.isNotBlank()
        }
    }

    /**
     * 密码合法性校验
     * 规则：长度 >5 位
     */
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }
}