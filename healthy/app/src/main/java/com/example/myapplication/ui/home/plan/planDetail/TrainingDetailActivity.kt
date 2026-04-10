package com.example.myapplication.ui.home.plan.planDetail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityTrainingDetailBinding

class TrainingDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTrainingDetailBinding

    // 计时器相关
    private var seconds = 0
    private var isRunning = false
    private val handler = Handler(Looper.getMainLooper())
    private val timerRunnable = object : Runnable {
        override fun run() {
            seconds++
            updateTimerText()
            handler.postDelayed(this, 1000)
        }
    }

    // 接收训练数据
    private lateinit var currentCourse: CourseItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTrainingDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 接收训练数据
        currentCourse = intent.getSerializableExtra("COURSE_ITEM") as CourseItem

        initToolbar()
        initTrainingContent()
        initGoButton()
        initTimerButtons()
    }

    private fun initToolbar() {
        binding.toolbar.title = currentCourse.courseName
        binding.toolbar.setNavigationOnClickListener { finish() }
    }

    // 显示完整训练内容
    private fun initTrainingContent() {
        binding.tvTrainingContent.text = currentCourse.content
    }

    // 初始化GO按钮：点击后显示计时器
    private fun initGoButton() {
        binding.btnGoTimer.setOnClickListener {
            // 隐藏GO按钮，显示计时器区域
            binding.btnGoTimer.visibility = android.view.View.GONE
            binding.llTimerArea.visibility = android.view.View.VISIBLE
            // 自动开始计时
            startTimer()
        }
    }

    // 初始化计时器按钮
    private fun initTimerButtons() {
        // 暂停/继续按钮（三角形）
        binding.btnPausePlay.setOnClickListener {
            if (isRunning) {
                pauseTimer()
            } else {
                startTimer()
            }
        }

        // 终止按钮（正方形）：重置计时，隐藏计时器，显示GO按钮
        binding.btnStop.setOnClickListener {
            stopTimer()
            binding.llTimerArea.visibility = android.view.View.GONE
            binding.btnGoTimer.visibility = android.view.View.VISIBLE
        }
    }

    private fun startTimer() {
        isRunning = true
        handler.postDelayed(timerRunnable, 1000)
        binding.btnPausePlay.setImageResource(android.R.drawable.ic_media_pause)
    }

    private fun pauseTimer() {
        isRunning = false
        handler.removeCallbacks(timerRunnable)
        binding.btnPausePlay.setImageResource(android.R.drawable.ic_media_play)
    }

    private fun stopTimer() {
        isRunning = false
        handler.removeCallbacks(timerRunnable)
        seconds = 0
        updateTimerText()
    }

    private fun updateTimerText() {
        val hours = seconds / 3600
        val minutes = (seconds % 3600) / 60
        val secs = seconds % 60
        binding.tvTimer.text = String.format(java.util.Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, secs)
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(timerRunnable)
    }

    companion object {
        fun actionStart(context: Context, courseItem: CourseItem) {
            val intent = Intent(context, TrainingDetailActivity::class.java).apply {
                putExtra("COURSE_ITEM", courseItem)
            }
            context.startActivity(intent)
        }
    }
}