package com.example.myapplication.ui.fitness

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.doOnLayout
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.R
import com.example.myapplication.data.model.Result
import com.example.myapplication.data.model.WorkoutDurationDailyStat
import com.example.myapplication.data.model.WorkoutDurationSummary
import com.example.myapplication.data.remote.WorkoutDurationDataSource
import com.example.myapplication.databinding.ActivityStatisticsBinding
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.math.max
import kotlin.math.roundToInt

class StatisticsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStatisticsBinding
    private val workoutDurationDataSource = WorkoutDurationDataSource()
    private val requestDateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private val displayDateFormatter = SimpleDateFormat("M/d", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStatisticsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initToolbar()
        loadProfileData()
        loadWorkoutSummary()
    }

    private fun initToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.stats_title)
        binding.toolbar.setNavigationOnClickListener { finish() }
    }

    private fun loadWorkoutSummary() {
        val endCalendar = Calendar.getInstance()
        val startCalendar = Calendar.getInstance().apply {
            add(Calendar.DAY_OF_YEAR, -6)
        }

        val startDate = requestDateFormatter.format(startCalendar.time)
        val endDate = requestDateFormatter.format(endCalendar.time)

        lifecycleScope.launch {
            binding.progressStats.visibility = View.VISIBLE
            binding.tvStatsError.visibility = View.GONE

            when (val result = workoutDurationDataSource.getSummary(startDate, endDate)) {
                is Result.Success -> renderWorkoutSummary(result.data)
                is Result.Error -> {
                    renderWorkoutSummary(
                        WorkoutDurationSummary(
                            totalDurationSeconds = 0,
                            planDurationSeconds = 0,
                            aiPlanDurationSeconds = 0,
                            activeDays = 0,
                            dailyRecords = emptyList()
                        )
                    )
                    binding.tvStatsError.visibility = View.VISIBLE
                    binding.tvStatsError.text = result.exception.message
                    Toast.makeText(
                        this@StatisticsActivity,
                        result.exception.message ?: getString(R.string.stats_load_failed),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            binding.progressStats.visibility = View.GONE
        }
    }

    private fun renderWorkoutSummary(summary: WorkoutDurationSummary) {
        binding.tvTotalDurationValue.text = formatDuration(summary.totalDurationSeconds)
        binding.tvPlanDurationValue.text = formatDuration(summary.planDurationSeconds)
        binding.tvAiDurationValue.text = formatDuration(summary.aiPlanDurationSeconds)
        binding.tvActiveDaysValue.text = getString(R.string.stats_active_days_value, summary.activeDays)

        val records = if (summary.dailyRecords.isNotEmpty()) {
            summary.dailyRecords
        } else {
            buildEmptyRecords()
        }
        renderChart(records)
    }

    private fun renderChart(records: List<WorkoutDurationDailyStat>) {
        binding.llChartContainer.removeAllViews()

        val maxDuration = max(1, records.maxOfOrNull { it.totalDurationSeconds } ?: 0)

        records.forEach { record ->
            val rowView = LayoutInflater.from(this)
                .inflate(R.layout.item_workout_duration_bar, binding.llChartContainer, false)

            val tvDate = rowView.findViewById<TextView>(R.id.tvDateLabel)
            val tvDuration = rowView.findViewById<TextView>(R.id.tvDurationValue)
            val tvBreakdown = rowView.findViewById<TextView>(R.id.tvSourceBreakdown)
            val barTrack = rowView.findViewById<View>(R.id.viewBarTrack)
            val barFill = rowView.findViewById<View>(R.id.viewBarFill)

            tvDate.text = formatDisplayDate(record.recordDate)
            tvDuration.text = formatMinutesShort(record.totalDurationSeconds)
            tvBreakdown.text = getString(
                R.string.stats_breakdown_template,
                formatMinutesShort(record.planDurationSeconds),
                formatMinutesShort(record.aiPlanDurationSeconds)
            )

            barTrack.doOnLayout { track ->
                val ratio = record.totalDurationSeconds.toFloat() / maxDuration.toFloat()
                val targetWidth = if (record.totalDurationSeconds <= 0) {
                    0
                } else {
                    max((track.width * ratio).roundToInt(), dpToPx(14))
                }
                barFill.layoutParams = barFill.layoutParams.apply {
                    width = targetWidth
                }
                barFill.visibility = if (targetWidth > 0) View.VISIBLE else View.INVISIBLE
            }

            binding.llChartContainer.addView(rowView)
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

    private fun loadProfileData() {
        val sp = getSharedPreferences("health_app", MODE_PRIVATE)
        val tableStats: TableLayout = binding.tableStats

        val height = sp.getFloat("height", 0f)
        val weight = sp.getFloat("weight", 0f)
        val bmi = sp.getFloat("bmi", 0f)
        addRow(tableStats, getString(R.string.stats_height), if (height == 0f) getString(R.string.stats_not_filled) else height.toString())
        addRow(tableStats, getString(R.string.stats_weight), if (weight == 0f) getString(R.string.stats_not_filled) else weight.toString())
        addRow(tableStats, "BMI", if (bmi == 0f) getString(R.string.stats_not_filled) else String.format(Locale.getDefault(), "%.1f", bmi))

        addRow(tableStats, getString(R.string.stats_injuries), sp.getString("injuries", getString(R.string.stats_none)) ?: getString(R.string.stats_none))
        addRow(tableStats, getString(R.string.stats_body_shape), sp.getString("body_shape", getString(R.string.stats_not_selected)) ?: getString(R.string.stats_not_selected))
        addRow(tableStats, getString(R.string.stats_goal), sp.getString("aim_goal", getString(R.string.stats_not_selected)) ?: getString(R.string.stats_not_selected))
        addRow(tableStats, getString(R.string.stats_focus_area), sp.getString("focus_area", getString(R.string.stats_not_selected)) ?: getString(R.string.stats_not_selected))
        addRow(tableStats, getString(R.string.stats_current_status), sp.getString("current_body_status", getString(R.string.stats_not_selected)) ?: getString(R.string.stats_not_selected))
        addRow(tableStats, getString(R.string.stats_target_status), sp.getString("target_body_status", getString(R.string.stats_not_selected)) ?: getString(R.string.stats_not_selected))
        addRow(tableStats, getString(R.string.stats_sport_type), sp.getString("sport_type", getString(R.string.stats_not_selected)) ?: getString(R.string.stats_not_selected))
        addRow(tableStats, getString(R.string.stats_daily_duration), sp.getString("duration", getString(R.string.stats_not_selected)) ?: getString(R.string.stats_not_selected))
        addRow(tableStats, getString(R.string.stats_requirement), sp.getString("requirement", getString(R.string.stats_not_selected)) ?: getString(R.string.stats_not_selected))
        addRow(tableStats, getString(R.string.stats_equipment), sp.getString("equipment", getString(R.string.stats_not_selected)) ?: getString(R.string.stats_not_selected))
        addRow(tableStats, getString(R.string.stats_pushup), sp.getString("pushup", getString(R.string.stats_not_selected)) ?: getString(R.string.stats_not_selected))
        addRow(tableStats, getString(R.string.stats_squat), sp.getString("squat", getString(R.string.stats_not_selected)) ?: getString(R.string.stats_not_selected))
        addRow(tableStats, getString(R.string.stats_situp), sp.getString("situp", getString(R.string.stats_not_selected)) ?: getString(R.string.stats_not_selected))
        addRow(tableStats, getString(R.string.stats_stairs), sp.getString("stairs", getString(R.string.stats_not_selected)) ?: getString(R.string.stats_not_selected))
    }

    private fun addRow(tableLayout: TableLayout, label: String, value: String) {
        val row = TableRow(this).apply {
            layoutParams = TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT
            )
            setPadding(dpToPx(6), dpToPx(10), dpToPx(6), dpToPx(10))
        }

        val tvLabel = TextView(this).apply {
            text = label
            setTextSize(android.util.TypedValue.COMPLEX_UNIT_SP, 14f)
            setTextColor(getColor(R.color.black))
            layoutParams = TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f)
        }

        val tvValue = TextView(this).apply {
            text = value
            setTextSize(android.util.TypedValue.COMPLEX_UNIT_SP, 14f)
            setTextColor(0xFF5F6368.toInt())
            layoutParams = TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1.6f)
        }

        row.addView(tvLabel)
        row.addView(tvValue)
        tableLayout.addView(row)

        val divider = View(this).apply {
            layoutParams = TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                dpToPx(1)
            )
            setBackgroundColor(0xFFEDEDED.toInt())
        }
        tableLayout.addView(divider)
    }

    private fun formatDisplayDate(rawDate: String): String {
        return runCatching {
            val parsed = requestDateFormatter.parse(rawDate)
            parsed?.let { displayDateFormatter.format(it) } ?: rawDate
        }.getOrDefault(rawDate)
    }

    private fun formatDuration(seconds: Int): String {
        if (seconds < 60) {
            return getString(R.string.stats_duration_second_only, seconds)
        }
        val totalMinutes = seconds / 60
        val hours = totalMinutes / 60
        val minutes = totalMinutes % 60
        return if (hours > 0) {
            getString(R.string.stats_duration_hour_minute, hours, minutes)
        } else {
            getString(R.string.stats_duration_minute_only, totalMinutes)
        }
    }

    private fun formatMinutesShort(seconds: Int): String {
        if (seconds < 60) {
            return getString(R.string.stats_seconds_short, seconds)
        }
        val totalMinutes = seconds / 60
        return getString(R.string.stats_minutes_short, totalMinutes)
    }

    private fun dpToPx(dp: Int): Int {
        return (dp * resources.displayMetrics.density).roundToInt()
    }
}
