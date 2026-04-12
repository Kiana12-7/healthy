package com.example.myapplication.data.remote

import android.util.Log
import com.example.myapplication.data.model.AIChatRequest
import com.example.myapplication.data.model.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class VitaDataSource {
    /**
     * 调用 Retrofit 接口，向后台请求生成训练计划
     * @return
     */
    suspend fun generatePlan(): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val response = RetrofitClient.vitaService.generatePlan()
                if (response.isSuccessful) {
                    Result.Success(Unit)
                } else {
                    Result.Error(IOException("生成计划失败：服务器错误 ${response.code()}"))
                }
            } catch (e: HttpException) {
                Log.e("Vita", "HTTP error: ${e.code()}", e)
                Result.Error(IOException("生成计划失败：服务器错误 ${e.code()}", e))
            } catch (e: IOException) {
                Log.e("Vita", "Network error", e)
                Result.Error(IOException("生成计划失败：网络异常", e))
            } catch (e: Exception) {
                Log.e("Vita", "Unknown error", e)
                Result.Error(IOException("生成计划失败：${e.message}", e))
            }
        }
    }

    suspend fun chat(message: String): Result<String> {
        return withContext(Dispatchers.IO) {
            try {
                val response = RetrofitClient.vitaService.chat(AIChatRequest(message))
                Result.Success(response.reply)
            } catch (e: HttpException) {
                Log.e("Vita", "HTTP error: ${e.code()}", e)
                Result.Error(IOException("问答失败：服务器错误 ${e.code()}", e))
            } catch (e: IOException) {
                Log.e("Vita", "Network error", e)
                Result.Error(IOException("问答失败：网络异常", e))
            } catch (e: Exception) {
                Log.e("Vita", "Unknown error", e)
                Result.Error(IOException("问答失败：${e.message}", e))
            }
        }
    }
}
