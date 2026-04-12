package com.example.myapplication.data.remote

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class FitnessFormDataSource {
    /**
     * 调用 Retrofit 接口，向后台请求保存
     * @return
     */
    suspend fun save(description: String) {  // 返回类型 Unit
        withContext(Dispatchers.IO) {
            try {
                val response = RetrofitClient.fitnessFormService.save(description)
            } catch (e: HttpException) {
                Log.e("FitnessForm", "HTTP error: ${e.code()}", e)
            } catch (e: IOException) {
                Log.e("FitnessForm", "Network error", e)
            } catch (e: Exception) {
                Log.e("FitnessForm", "Unknown error", e)
            }
        }
    }
}