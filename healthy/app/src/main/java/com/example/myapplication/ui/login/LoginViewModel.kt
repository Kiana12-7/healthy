package com.example.myapplication.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.repository.LoginRepository
import com.example.myapplication.data.model.Result
import com.example.myapplication.R
import kotlinx.coroutines.launch

class LoginViewModel(private val loginRepository: LoginRepository) : ViewModel() {

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    fun login(username: String, password: String) {
        viewModelScope.launch {
            val result = loginRepository.login(username, password)

            if (result is Result.Success) {
                _loginResult.postValue(
                    LoginResult(success = LoggedInUserView(displayName = result.data.displayName))
                )
            } else {
                val message = (result as? Result.Error)?.exception?.message
                    ?: "登录失败，请稍后重试"
                _loginResult.postValue(LoginResult(errorMessage = message))
            }
        }
    }

    fun register(username: String, password: String) {
        viewModelScope.launch {
            val result = loginRepository.register(username, password)

            if (result is Result.Success) {
                _loginResult.postValue(
                    LoginResult(success = LoggedInUserView(displayName = result.data.displayName))
                )
            } else {
                val message = (result as? Result.Error)?.exception?.message
                    ?: "注册失败，请稍后重试"
                _loginResult.postValue(LoginResult(errorMessage = message))
            }
        }
    }

    fun loginDataChanged(username: String, password: String) {
        if (username.isBlank()) {
            _loginForm.value = LoginFormState(usernameError = R.string.invalid_username)
            return
        }
        if (password.length < 6) {
            _loginForm.value = LoginFormState(passwordError = R.string.invalid_password)
            return
        }
        _loginForm.value = LoginFormState(isDataValid = true)
    }
}
