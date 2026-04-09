package com.example.myapplication.data.remote

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class VitaDataSource {
    /**
     * 调用 Retrofit 接口，向后台请求生成训练计划
     * @return
     */
    suspend fun generatePlan() {  // 返回类型 Unit
        withContext(Dispatchers.IO) {
            try {
                val response = RetrofitClient.vitaService.generatePlan()
            } catch (e: HttpException) {
                Log.e("Vita", "HTTP error: ${e.code()}", e)
            } catch (e: IOException) {
                Log.e("Vita", "Network error", e)
            } catch (e: Exception) {
                Log.e("Vita", "Unknown error", e)
            }
        }
    }
}