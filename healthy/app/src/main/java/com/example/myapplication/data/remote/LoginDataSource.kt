package com.example.myapplication.data.remote

import com.example.myapplication.data.model.LoggedInUser
import com.example.myapplication.data.model.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

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
        return withContext(Dispatchers.IO) {
            try {
                val response = RetrofitClient.userService.login(username, password)
                Result.Success(response)
            } catch (e: HttpException) {
                Result.Error(IOException(resolveAuthMessage("登录", e), e))
            } catch (e: IOException) {
                Result.Error(IOException("登录失败：网络异常", e))
            } catch (e: Exception) {
                Result.Error(IOException("登录失败：${e.message}", e))
            }
        }
    }

    suspend fun register(username: String, password: String): Result<LoggedInUser> {
        return withContext(Dispatchers.IO) {
            try {
                val response = RetrofitClient.userService.register(username, password)
                Result.Success(response)
            } catch (e: HttpException) {
                Result.Error(IOException(resolveAuthMessage("注册", e), e))
            } catch (e: IOException) {
                Result.Error(IOException("注册失败：网络异常", e))
            } catch (e: Exception) {
                Result.Error(IOException("注册失败：${e.message}", e))
            }
        }
    }

    suspend fun logout(): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                RetrofitClient.userService.logout()
                RetrofitClient.clearCookies()
                Result.Success(Unit)
            } catch (e: HttpException) {
                if (e.code() == 401 || e.code() == 403) {
                    RetrofitClient.clearCookies()
                    Result.Success(Unit)
                } else {
                    Result.Error(IOException("退出失败：服务器错误 ${e.code()}", e))
                }
            } catch (e: IOException) {
                Result.Error(IOException("退出失败：网络异常", e))
            } catch (e: Exception) {
                Result.Error(IOException("退出失败：${e.message}", e))
            }
        }
    }

    private fun resolveAuthMessage(action: String, exception: HttpException): String {
        return when (exception.code()) {
            400 -> "${action}失败：用户名不能为空，密码至少 6 位"
            401 -> "${action}失败：用户名或密码错误"
            409 -> "${action}失败：用户名已存在"
            else -> "${action}失败：服务器错误 ${exception.code()}"
        }
    }
}
