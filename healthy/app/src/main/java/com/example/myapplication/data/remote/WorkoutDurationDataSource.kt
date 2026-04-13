package com.example.myapplication.data.remote

import com.example.myapplication.data.model.Result
import com.example.myapplication.data.model.WorkoutDurationRecordRequest
import com.example.myapplication.data.model.WorkoutDurationSummary
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class WorkoutDurationDataSource {
    suspend fun getSummary(startDate: String, endDate: String): Result<WorkoutDurationSummary> {
        return withContext(Dispatchers.IO) {
            try {
                Result.Success(RetrofitClient.workoutDurationService.getSummary(startDate, endDate))
            } catch (e: HttpException) {
                Result.Error(IOException("统计数据加载失败：服务器错误 ${e.code()}", e))
            } catch (e: IOException) {
                Result.Error(IOException("统计数据加载失败：网络异常", e))
            } catch (e: Exception) {
                Result.Error(IOException("统计数据加载失败：${e.message}", e))
            }
        }
    }

    suspend fun recordDuration(request: WorkoutDurationRecordRequest): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                RetrofitClient.workoutDurationService.recordDuration(request)
                Result.Success(Unit)
            } catch (e: HttpException) {
                Result.Error(IOException("记录锻炼时长失败：服务器错误 ${e.code()}", e))
            } catch (e: IOException) {
                Result.Error(IOException("记录锻炼时长失败：网络异常", e))
            } catch (e: Exception) {
                Result.Error(IOException("记录锻炼时长失败：${e.message}", e))
            }
        }
    }
}
