package com.example.myapplication.data.repository

import com.example.myapplication.data.remote.LoginDataSource
import com.example.myapplication.data.model.Result
import com.example.myapplication.data.model.LoggedInUser

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 * 登录模块的仓库层
 */
class LoginRepository(val dataSource: LoginDataSource) {

    // in-memory cache of the loggedInUser object
    // 缓存当前已登录的用户信息（App 运行期间有效，退出即清空）
    var user: LoggedInUser? = null
        private set // 私有化 set，外部只能读取，不能直接修改

    // 判断用户是否已登录
    val isLoggedIn: Boolean
        get() = user != null

    init {
        // 初始化时清空用户缓存
        user = null
    }

    /**
     * 退出登录
     * 清空内存缓存的用户信息
     * 调用数据源执行登出逻辑
     */
    suspend fun logout(): Result<Unit> {
        val result = dataSource.logout()
        if (result is Result.Success) {
            user = null
        }
        return result
    }

    /**
     * 登录
     * @param username 用户名
     * @param password 密码
     * @return Result<LoggedInUser> 统一结果封装（成功/失败）
     */
    suspend fun login(username: String, password: String): Result<LoggedInUser> {
        val result = dataSource.login(username, password)

        if (result is Result.Success) {
            setLoggedInUser(result.data)
        }

        return result
    }

    suspend fun register(username: String, password: String): Result<LoggedInUser> {
        val result = dataSource.register(username, password)

        if (result is Result.Success) {
            setLoggedInUser(result.data)
        }

        return result
    }

    /**
     * 保存登录用户到内存缓存
     */
    private fun setLoggedInUser(loggedInUser: LoggedInUser) {
        this.user = loggedInUser
    }
}
