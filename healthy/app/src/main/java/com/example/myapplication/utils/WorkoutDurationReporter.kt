package com.example.myapplication.utils

import android.util.Log
import com.example.myapplication.data.model.WorkoutDurationRecordRequest
import com.example.myapplication.data.model.WorkoutDurationSourceType
import com.example.myapplication.data.remote.RetrofitClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object WorkoutDurationReporter {
    private val reporterScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    fun currentRecordDate(date: Date = Date()): String = dateFormatter.format(date)

    fun reportSession(sourceType: WorkoutDurationSourceType, startedAtMs: Long, recordDate: String) {
        val durationSeconds = (((android.os.SystemClock.elapsedRealtime() - startedAtMs) / 1000L).toInt())
        if (durationSeconds <= 0) {
            return
        }

        reporterScope.launch {
            try {
                RetrofitClient.workoutDurationService.recordDuration(
                    WorkoutDurationRecordRequest(
                        recordDate = recordDate,
                        durationSeconds = durationSeconds,
                        sourceType = sourceType.apiValue
                    )
                )
            } catch (e: Exception) {
                Log.e("WorkoutDuration", "Failed to report session", e)
            }
        }
    }
}
