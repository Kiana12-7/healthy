package com.example.myapplication.ui.home.plan.planDetail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.databinding.ActivityPlanDetailBinding
import com.example.myapplication.ui.home.plan.planItem.PlanItem
import java.util.Locale

class PlanDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPlanDetailBinding
    private lateinit var courseAdapter: CourseListAdapter

    private var planId: String = ""
    private var planName: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlanDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        intent.extras?.let {
            planId = it.getString("plan_id", "")
            planName = it.getString("plan_name", "")
        }

        initToolbar()
        initRecyclerView()
        loadPlanDetailData()
    }

    private fun initToolbar() {
        binding.toolbar.title = planName
        binding.toolbar.setNavigationOnClickListener { finish() }
    }

    private fun initRecyclerView() {
        courseAdapter = CourseListAdapter()
        binding.rvCourseList.apply {
            layoutManager = LinearLayoutManager(this@PlanDetailActivity)
            adapter = courseAdapter
        }

        // 点击训练项 → 跳训练详情页
        courseAdapter.setOnItemClickListener { courseItem ->
            TrainingDetailActivity.actionStart(this@PlanDetailActivity, courseItem)
        }
    }



    private fun loadPlanDetailData() {
        val mockPlanDetail = getMockPlanDetail(planId)
        binding.tvTotalCourse.text = String.format(Locale.getDefault(), "%d 节课程", mockPlanDetail.totalCourseCount)
        courseAdapter.setData(mockPlanDetail.courseList)
    }

    // 更新训练数据：短名称+完整步骤
    private fun getMockPlanDetail(planId: String): PlanDetail {
        val planItem = PlanItem.getAllPlans().firstOrNull { it.id == planId }
        val planName = planItem?.name ?: "个性减脂计划"

        // 个性减脂计划专属5节训练（列表短名+详情完整步骤）
        val fatLossCourseList = listOf(
            CourseItem(
                courseId = "${planId}_001",
                planId = planId,
                courseName = "全身激活燃脂循环",
                content = "【训练步骤】\n1. 开合跳 20次 × 4组\n2. 高抬腿 30秒 × 4组\n3. 勾脚跳 25次 × 4组\n✅ 组间休息：45秒\n💡 训练要点：动作连贯，保持呼吸节奏",
                duration = 20,
                difficulty = "易",
                isLearned = false,
                videoUrl = "https://xxx.com/video1.mp4"
            ),
            CourseItem(
                courseId = "${planId}_002",
                planId = planId,
                courseName = "核心燃脂进阶",
                content = "【训练步骤】\n1. 平板支撑 40秒 × 3组\n2. 登山跑 30秒 × 4组\n3. 侧支撑转体 20次/侧 × 3组\n✅ 组间休息：1分钟\n💡 训练要点：核心收紧，避免塌腰",
                duration = 25,
                difficulty = "中",
                isLearned = false,
                videoUrl = "https://xxx.com/video2.mp4"
            ),
            CourseItem(
                courseId = "${planId}_003",
                planId = planId,
                courseName = "下肢燃脂塑形",
                content = "【训练步骤】\n1. 徒手深蹲 20次 × 4组\n2. 箭步蹲 15次/侧 × 4组\n3. 臀桥 30次 × 4组\n✅ 组间休息：1分钟\n💡 训练要点：膝盖不超过脚尖，臀部发力",
                duration = 30,
                difficulty = "中",
                isLearned = false,
                videoUrl = "https://xxx.com/video3.mp4"
            ),
            CourseItem(
                courseId = "${planId}_004",
                planId = planId,
                courseName = "全身循环燃脂",
                content = "【训练步骤】\n1. 波比跳 12次 × 4组\n2. 弓步跳 15次/侧 × 3组\n3. 平板开合跳 20次 × 4组\n✅ 组间休息：1分钟\n💡 训练要点：量力而行，避免受伤",
                duration = 35,
                difficulty = "中",
                isLearned = false,
                videoUrl = "https://xxx.com/video4.mp4"
            ),
            CourseItem(
                courseId = "${planId}_005",
                planId = planId,
                courseName = "高强度间歇燃脂",
                content = "【训练步骤】\n1. 冲刺高抬腿 20秒 × 6组\n2. 开合跳 30秒 × 6组\n3. 俯身登山 20秒 × 6组\n✅ 组间休息：30秒\n💡 训练要点：全力冲刺，高效燃脂",
                duration = 40,
                difficulty = "难",
                isLearned = false,
                videoUrl = "https://xxx.com/video5.mp4"
            )
        )

        return PlanDetail(
            planId = planId,
            planName = planName,
            totalCourseCount = fatLossCourseList.size,
            courseList = fatLossCourseList
        )
    }

    companion object {
        fun actionStart(context: Context, planId: String, planName: String) {
            val intent = Intent(context, PlanDetailActivity::class.java).apply {
                putExtra("plan_id", planId)
                putExtra("plan_name", planName)
            }
            context.startActivity(intent)
        }
    }
}