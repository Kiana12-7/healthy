package com.example.myapplication.ui.home.plan.planDetail

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.databinding.ActivityTrainingDetailBinding
import java.util.Locale
import androidx.constraintlayout.widget.ConstraintSet
import android.view.View
import com.example.myapplication.data.model.CourseItem as VideoCourseItem
import com.example.myapplication.ui.video.VideoDetailActivity

class TrainingDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTrainingDetailBinding
    private lateinit var actionAdapter: TrainActionAdapter

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

    private lateinit var currentCourse: CourseItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTrainingDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        currentCourse = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra("COURSE_ITEM", CourseItem::class.java)!!
        } else {
            @Suppress("DEPRECATION")
            intent.getSerializableExtra("COURSE_ITEM") as CourseItem
        }

        initToolbar()
        initActionList()
        initTimerButton()
        initTimerButtons()
    }

    private fun initToolbar() {
        binding.toolbar.title = currentCourse.courseName
        binding.toolbar.setNavigationOnClickListener { finish() }
    }

    private fun initActionList() {
        actionAdapter = TrainActionAdapter()
        binding.rvActionList.layoutManager = LinearLayoutManager(this)
        binding.rvActionList.adapter = actionAdapter
        actionAdapter.setData(currentCourse.actionList)

        // 直接复用课程视频详情页，保证动作视频可播放
        actionAdapter.setOnWatchActionClickListener { actionItem ->
            val intent = Intent(this@TrainingDetailActivity, VideoDetailActivity::class.java).apply {
                putExtra("VIDEO_ITEM", actionItem.toPlayableVideo())
            }
            startActivity(intent)
        }
    }

    private fun initTimerButton() {
        binding.btnGoTimer.setOnClickListener {
            binding.btnGoTimer.visibility = View.GONE
            binding.llTimerArea.visibility = View.VISIBLE
            startTimer()
        }
    }

    private fun initTimerButtons() {
        binding.btnPausePlay.setOnClickListener {
            if (isRunning) pauseTimer() else startTimer()
        }
        binding.btnStop.setOnClickListener {
            stopTimer()
            binding.llTimerArea.visibility = View.GONE
            binding.btnGoTimer.visibility = View.VISIBLE
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
        binding.tvTimer.text = String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, secs)
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(timerRunnable)
    }

    private fun moveTimerToTop() {
        val constraintSet = ConstraintSet()
        constraintSet.clone(binding.root)
        constraintSet.clear(binding.llTimerArea.id, ConstraintSet.BOTTOM)
        constraintSet.connect(binding.llTimerArea.id, ConstraintSet.TOP, binding.toolbar.id, ConstraintSet.BOTTOM, 16)
        constraintSet.applyTo(binding.root)
    }

    private fun moveTimerToBottom() {
        val constraintSet = ConstraintSet()
        constraintSet.clone(binding.root)
        constraintSet.clear(binding.llTimerArea.id, ConstraintSet.TOP)
        constraintSet.connect(binding.llTimerArea.id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 0)
        constraintSet.applyTo(binding.root)
    }

    companion object {
        fun actionStart(context: Context, courseItem: CourseItem) {
            val intent = Intent(context, TrainingDetailActivity::class.java).apply {
                putExtra("COURSE_ITEM", courseItem)
            }
            context.startActivity(intent)
        }
    }

    private fun TrainActionItem.toPlayableVideo(): VideoCourseItem.TrainingVideo {
        return VideoCourseItem.TrainingVideo(
            id = actionId.toIntOrNull() ?: 0,
            title = actionName,
            trainerName = currentCourse.courseName,
            coverUrl = currentCourse.coverUrl.orEmpty(),
            videoUrl = videoUrl,
            duration = 0,
            difficultyTag = currentCourse.difficulty,
            preparePose = "先跟随视频调整到「$actionName」的起始姿势，再开始正式动作。",
            actionProcess = actionDesc,
            breathRhythm = "动作过程中保持均匀呼吸，发力时呼气，还原时吸气。",
            attention = "$groupDesc，$restDesc。动作标准优先于速度。"
        )
    }
}
