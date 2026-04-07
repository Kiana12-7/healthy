package com.example.myapplication.data.remote

import com.example.myapplication.data.model.LoggedInUser
import com.example.myapplication.data.model.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import java.util.UUID

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 * 登录数据源
 * 执行登录网络请求，从后台服务器获取数据
 */
class LoginDataSource {
    /**
     * 调用 Retrofit 接口，向后台发送用户名、密码，获取登录结果
     * @param username 用户名
     * @param password 密码
     * @return Result<LoggedInUser> 统一返回成功/失败
     */
    suspend fun login(username: String, password: String): Result<LoggedInUser> {
        // 切换到IO线程执行网络请求
        return withContext(Dispatchers.IO) {
            try {
                // 调用 Retrofit 的登录接口，把账号密码传给后台
                val response = RetrofitClient.userService.login(username, password)

                // 后台返回成功，包装成 Result.Success
                Result.Success(response)

            } catch (e: HttpException) {
                // HTTP 错误（如 400/401/404/500）
                Result.Error(IOException("登录失败：服务器错误 ${e.code()}", e))
            } catch (e: IOException) {
                // 网络错误（无网、超时、连接失败）
                Result.Error(IOException("登录失败：网络异常", e))
            } catch (e: Exception) {
                // 其他未知错误
                Result.Error(IOException("登录失败：${e.message}", e))
            }
        }
    }

    fun logout() {
        // TODO: revoke authentication
    }
}