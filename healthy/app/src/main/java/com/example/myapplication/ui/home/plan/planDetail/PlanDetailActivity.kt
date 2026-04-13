package com.example.myapplication.ui.home.plan.planDetail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.data.model.WorkoutPlanActionDto
import com.example.myapplication.data.model.WorkoutPlanCourseDto
import com.example.myapplication.data.model.WorkoutPlanDetailDto
import com.example.myapplication.data.remote.RetrofitClient
import com.example.myapplication.databinding.ActivityPlanDetailBinding
import kotlinx.coroutines.launch

class PlanDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlanDetailBinding
    private lateinit var courseAdapter: CourseListAdapter

    // 接收 planId 和 planName
    private val planId: String by lazy {
        intent.getStringExtra(KEY_PLAN_ID) ?: ""
    }
    private val planName: String by lazy {
        intent.getStringExtra(KEY_PLAN_NAME) ?: "健身计划"
    }

    companion object {
        const val KEY_PLAN_ID = "key_plan_id"
        const val KEY_PLAN_NAME = "key_plan_name"

        fun actionStart(context: Context, planId: String, planName: String) {
            val intent = Intent(context, PlanDetailActivity::class.java).apply {
                putExtra(KEY_PLAN_ID, planId)
                putExtra(KEY_PLAN_NAME, planName)
            }
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlanDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initTopBar()
        initActionList()
        loadPlanDetail()
    }

    private fun initTopBar() {
        binding.toolbar.title = planName
        binding.toolbar.setNavigationOnClickListener { finish() }
    }

    private fun initActionList() {
        binding.rvCourseList.layoutManager = LinearLayoutManager(this)
        courseAdapter = CourseListAdapter()

        courseAdapter.setOnItemClickListener { courseItem ->
            TrainingDetailActivity.actionStart(this@PlanDetailActivity, courseItem)
        }

        binding.rvCourseList.adapter = courseAdapter
    }

    private fun loadPlanDetail() {
        if (planId.isEmpty()) {
            Toast.makeText(this, "计划 ID 为空", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // --- ID 清洗逻辑：处理 plan_001 这种格式 ---
        val targetPlanId: Long? = if (planId.startsWith("plan_")) {
            planId.substringAfter("_").toLongOrNull()
        } else {
            planId.toLongOrNull()
        }

        if (targetPlanId == null) {
            Toast.makeText(this, "无效的计划 ID 格式: $planId", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        lifecycleScope.launch {
            try {
                val response = RetrofitClient.workoutPlanService.getWorkoutPlanDetail(targetPlanId)
                renderPlanDetail(response)
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this@PlanDetailActivity, "详情加载失败（ID:$targetPlanId）", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun renderPlanDetail(detail: WorkoutPlanDetailDto) {
        binding.toolbar.title = detail.planName
        courseAdapter.setData(detail.courseList.map { it.toUiCourseItem() })
    }

    // 内部转换方法 1
    private fun WorkoutPlanCourseDto.toUiCourseItem(): CourseItem {
        return CourseItem(
            courseId = courseId,
            planId = planId.toString(),
            courseName = courseName,
            actionList = actionList.map { it.toUiActionItem() }, // 调用下面的方法
            duration = duration,
            difficulty = difficulty,
            isLearned = learned,
            videoUrl = videoUrl,
            coverUrl = coverUrl
        )
    }

    // 内部转换方法 2 (修正了关键字 fun)
    private fun WorkoutPlanActionDto.toUiActionItem(): TrainActionItem {
        return TrainActionItem(
            actionId = actionId.toString(),
            actionName = actionName,
            groupDesc = groupDesc,
            restDesc = restDesc,
            videoUrl = videoUrl,
            actionDesc = actionDesc
        )
    }
}