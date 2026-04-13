package com.example.myapplication.ui.personal

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.doOnLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.R
import com.example.myapplication.data.model.CurrentWorkoutPlanSummary
import com.example.myapplication.data.model.Result
import com.example.myapplication.data.model.WorkoutDurationDailyStat
import com.example.myapplication.data.model.WorkoutDurationSummary
import com.example.myapplication.data.remote.LoginDataSource
import com.example.myapplication.data.remote.RetrofitClient
import com.example.myapplication.data.remote.WorkoutDurationDataSource
import com.example.myapplication.data.repository.LoginRepository
import com.example.myapplication.ui.fitness.StatisticsActivity
import com.example.myapplication.ui.login.LoginActivity
import com.example.myapplication.ui.login.UserViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.math.max
import kotlin.math.roundToInt

class PersonalFragment : Fragment() {
    private lateinit var userViewModel: UserViewModel
    private lateinit var ivAvatar: ImageView
    private lateinit var tvNickname: TextView
    private lateinit var tvDays: TextView
    private lateinit var btnLogout: ImageView
    private lateinit var tvCurrentPlanName: TextView
    private lateinit var tvCurrentPlanProgress: TextView
    private lateinit var tvCurrentPlanDate: TextView
    private lateinit var tvCurrentPlanDescription: TextView
    private lateinit var tvTrainingTotalDuration: TextView
    private lateinit var tvTrainingActiveDays: TextView
    private lateinit var tvTrainingPlanDuration: TextView
    private lateinit var tvTrainingAiDuration: TextView
    private lateinit var chartContainer: LinearLayout
    private lateinit var itemCourses: LinearLayout
    private var itemMyData: LinearLayout? = null
    private val loginRepository by lazy {
        LoginRepository(LoginDataSource())
    }
    private val workoutDurationDataSource by lazy {
        WorkoutDurationDataSource()
    }
    private val requestDateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private val displayDateFormatter = SimpleDateFormat("M/d", Locale.getDefault())

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return try {
            val view = inflater.inflate(R.layout.fragment_personal, container, false)
            ivAvatar = view.findViewById(R.id.iv_avatar)
            tvNickname = view.findViewById(R.id.tv_nickname)
            tvDays = view.findViewById(R.id.tv_days)
            btnLogout = view.findViewById(R.id.btn_logout)
            tvCurrentPlanName = view.findViewById(R.id.tv_current_plan_name)
            tvCurrentPlanProgress = view.findViewById(R.id.tv_current_plan_progress)
            tvCurrentPlanDate = view.findViewById(R.id.tv_current_plan_date)
            tvCurrentPlanDescription = view.findViewById(R.id.tv_current_plan_description)
            tvTrainingTotalDuration = view.findViewById(R.id.tv_training_total_duration)
            tvTrainingActiveDays = view.findViewById(R.id.tv_training_active_days)
            tvTrainingPlanDuration = view.findViewById(R.id.tv_training_plan_duration)
            tvTrainingAiDuration = view.findViewById(R.id.tv_training_ai_duration)
            chartContainer = view.findViewById(R.id.ll_personal_chart_container)
            itemCourses = view.findViewById(R.id.item_courses)
            itemMyData = view.findViewById(R.id.item_my_data)

            userViewModel = ViewModelProvider(this)[UserViewModel::class.java]

            userViewModel.user.observe(viewLifecycleOwner) { user ->
                if (user != null) {
                    tvNickname.text = user.name
                    tvDays.text = "累计运动 ${user.days} 天"
                } else {
                    tvNickname.text = "Keep 运动达人"
                    tvDays.text = "累计运动 0 天"
                }
            }

            view
        } catch (e: Exception) {
            e.printStackTrace()
            // 如果布局加载失败，返回一个简单视图避免崩溃
            TextView(requireContext()).apply {
                text = "页面加载失败，请检查布局资源"
                setPadding(50, 50, 50, 50)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupClickListeners()
    }

    private fun setupClickListeners() {
        itemCourses.setOnClickListener {
            showToast("点击了我的课程")
        }

        itemMyData?.setOnClickListener {
            try {
                val intent = Intent(requireContext(), StatisticsActivity::class.java)
                startActivity(intent)
            } catch (e: Exception) {
                showToast("无法打开统计数据页面，请检查 Activity 是否注册")
                e.printStackTrace()
            }
        }

        ivAvatar.setOnClickListener {
            showToast("点击了头像")
        }

        btnLogout.setOnClickListener {
            logout()
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun logout() {
        btnLogout.isEnabled = false

        viewLifecycleOwner.lifecycleScope.launch {
            when (val result = loginRepository.logout()) {
                is Result.Success -> {
                    showToast(getString(R.string.logout_success))
                    val intent = Intent(requireContext(), LoginActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    }
                    startActivity(intent)
                    requireActivity().finish()
                }

                is Result.Error -> {
                    btnLogout.isEnabled = true
                    showToast(result.exception.message ?: getString(R.string.logout_failed))
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        try {
            userViewModel.me()
            loadPersonalData()
        } catch (e: Exception) {
            e.printStackTrace()
            // 不崩溃，仅记录日志
        }
    }

    private fun loadPersonalData() {
        val endCalendar = Calendar.getInstance()
        val startCalendar = Calendar.getInstance().apply {
            add(Calendar.DAY_OF_YEAR, -6)
        }
        val today = requestDateFormatter.format(endCalendar.time)
        val startDate = requestDateFormatter.format(startCalendar.time)

        renderWorkoutSummary(
            WorkoutDurationSummary(
                totalDurationSeconds = 0,
                planDurationSeconds = 0,
                aiPlanDurationSeconds = 0,
                activeDays = 0,
                dailyRecords = buildEmptyRecords()
            )
        )
        renderCurrentPlan(null)

        viewLifecycleOwner.lifecycleScope.launch {
            when (val result = workoutDurationDataSource.getSummary(startDate, today)) {
                is Result.Success -> renderWorkoutSummary(result.data)
                is Result.Error -> renderWorkoutSummary(
                    WorkoutDurationSummary(
                        totalDurationSeconds = 0,
                        planDurationSeconds = 0,
                        aiPlanDurationSeconds = 0,
                        activeDays = 0,
                        dailyRecords = buildEmptyRecords()
                    )
                )
            }

            renderCurrentPlan(
                runCatching { RetrofitClient.workoutPlanService.getCurrentPlanSummary(today) }.getOrNull()
            )
        }
    }

    private fun renderCurrentPlan(summary: CurrentWorkoutPlanSummary?) {
        if (summary == null || !summary.hasActivePlan) {
            tvCurrentPlanName.text = getString(R.string.personal_plan_empty_title)
            tvCurrentPlanProgress.text = getString(R.string.personal_plan_empty_progress)
            tvCurrentPlanDate.text = getString(R.string.personal_plan_empty_date)
            tvCurrentPlanDescription.text = getString(R.string.personal_plan_empty_description)
            return
        }

        tvCurrentPlanName.text = summary.planName ?: getString(R.string.personal_plan_default_title)
        tvCurrentPlanProgress.text = getString(
            R.string.personal_plan_progress_template,
            summary.currentDay ?: 0,
            summary.totalDays ?: 0
        )
        tvCurrentPlanDate.text = getString(
            R.string.personal_plan_date_template,
            formatDate(summary.startDate),
            formatDate(summary.endDate)
        )
        tvCurrentPlanDescription.text = getString(
            R.string.personal_plan_description_template,
            summary.currentDay ?: 0
        )
    }

    private fun renderWorkoutSummary(summary: WorkoutDurationSummary) {
        tvTrainingTotalDuration.text = formatDuration(summary.totalDurationSeconds)
        tvTrainingActiveDays.text = getString(R.string.personal_training_active_days, summary.activeDays)
        tvTrainingPlanDuration.text = getString(
            R.string.personal_training_split_template,
            getString(R.string.personal_training_plan_label),
            formatShortDuration(summary.planDurationSeconds)
        )
        tvTrainingAiDuration.text = getString(
            R.string.personal_training_split_template,
            getString(R.string.personal_training_ai_label),
            formatShortDuration(summary.aiPlanDurationSeconds)
        )

        val records = if (summary.dailyRecords.isNotEmpty()) summary.dailyRecords else buildEmptyRecords()
        renderChart(records)
    }

    private fun renderChart(records: List<WorkoutDurationDailyStat>) {
        chartContainer.removeAllViews()
        val maxDuration = max(1, records.maxOfOrNull { it.totalDurationSeconds } ?: 0)

        records.forEach { record ->
            val rowView = LayoutInflater.from(requireContext())
                .inflate(R.layout.item_workout_duration_bar, chartContainer, false)

            val tvDate = rowView.findViewById<TextView>(R.id.tvDateLabel)
            val tvDuration = rowView.findViewById<TextView>(R.id.tvDurationValue)
            val tvBreakdown = rowView.findViewById<TextView>(R.id.tvSourceBreakdown)
            val barTrack = rowView.findViewById<View>(R.id.viewBarTrack)
            val barFill = rowView.findViewById<View>(R.id.viewBarFill)

            tvDate.text = formatDate(record.recordDate)
            tvDuration.text = formatShortDuration(record.totalDurationSeconds)
            tvBreakdown.text = getString(
                R.string.personal_training_chart_breakdown,
                formatShortDuration(record.planDurationSeconds),
                formatShortDuration(record.aiPlanDurationSeconds)
            )

            barTrack.doOnLayout { track ->
                val ratio = record.totalDurationSeconds.toFloat() / maxDuration.toFloat()
                val targetWidth = if (record.totalDurationSeconds <= 0) {
                    0
                } else {
                    max((track.width * ratio).roundToInt(), dpToPx(12))
                }
                barFill.layoutParams = barFill.layoutParams.apply {
                    width = targetWidth
                }
                barFill.visibility = if (targetWidth > 0) View.VISIBLE else View.INVISIBLE
            }

            chartContainer.addView(rowView)
        }
    }

    private fun buildEmptyRecords(): List<WorkoutDurationDailyStat> {
        val calendar = Calendar.getInstance().apply {
            add(Calendar.DAY_OF_YEAR, -6)
        }
        return buildList {
            repeat(7) {
                add(
                    WorkoutDurationDailyStat(
                        recordDate = requestDateFormatter.format(calendar.time),
                        planDurationSeconds = 0,
                        aiPlanDurationSeconds = 0,
                        totalDurationSeconds = 0
                    )
                )
                calendar.add(Calendar.DAY_OF_YEAR, 1)
            }
        }
    }

    private fun formatDate(rawDate: String?): String {
        if (rawDate.isNullOrBlank()) {
            return getString(R.string.personal_plan_empty_date_short)
        }
        return runCatching {
            val parsed = requestDateFormatter.parse(rawDate)
            parsed?.let { displayDateFormatter.format(it) } ?: rawDate
        }.getOrDefault(rawDate)
    }

    private fun formatDuration(seconds: Int): String {
        if (seconds < 60) {
            return getString(R.string.stats_duration_second_only, seconds)
        }
        val minutes = seconds / 60
        val hours = minutes / 60
        val remainMinutes = minutes % 60
        return if (hours > 0) {
            getString(R.string.stats_duration_hour_minute, hours, remainMinutes)
        } else {
            getString(R.string.stats_duration_minute_only, minutes)
        }
    }

    private fun formatShortDuration(seconds: Int): String {
        return if (seconds < 60) {
            getString(R.string.stats_seconds_short, seconds)
        } else {
            getString(R.string.stats_minutes_short, seconds / 60)
        }
    }

    private fun dpToPx(dp: Int): Int {
        return (dp * resources.displayMetrics.density).roundToInt()
    }
}
